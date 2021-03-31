package manage.command;

import common.DatabaseUtils;
import common.Repository;
import common.domain.Subscription;
import common.domain.User;
import manage.command.model.Command;
import manage.command.model.CreateUserCommand;
import manage.command.model.ExtendCommand;
import manage.command.model.GiveCommand;

import java.sql.SQLException;
import java.time.LocalDate;

public class CommandExecutor {
    private final CommandDAO commandDAO;
    private final Repository repository;

    public CommandExecutor(CommandDAO commandDAO, Repository repository) {
        this.commandDAO = commandDAO;
        this.repository = repository;
    }

    public String executeCommand(Command command) throws SQLException {
        if (command instanceof CreateUserCommand) {
            CreateUserCommand createUserCommand = (CreateUserCommand) command;

            int userId = commandDAO.createUser(createUserCommand.getName());
            return "User '%s' with id '%d' created".formatted(createUserCommand.getName(), userId);
        }

        if (command instanceof ExtendCommand) {
            ExtendCommand extendCommand = (ExtendCommand) command;

            User user = repository.getUser(extendCommand.getUserId());

            if (user == null) {
                return "No such user!";
            }

            Subscription subscription = repository.getLastSubscription(extendCommand.getUserId());

            if (subscription == null || subscription.getDueDate().isBefore(LocalDate.now())) {
                return "No active subscription for user: '%d'".formatted(extendCommand.getUserId());
            }


            LocalDate due = subscription.getDueDate().plusDays(extendCommand.getDays());
            commandDAO.extendSubscription(extendCommand.getUserId(), due);
            return "Subscription for user: '%s' extended until '%s'".formatted(user.getName(), DatabaseUtils.formatLocalDate(due));
        }

        if (command instanceof GiveCommand) {
            GiveCommand giveCommand = (GiveCommand) command;

            User user = repository.getUser(giveCommand.getUserId());

            if (user == null) {
                return "No such user!";
            }

            Subscription subscription = repository.getLastSubscription(giveCommand.getUserId());

            if (subscription != null && subscription.getDueDate().isAfter(LocalDate.now())) {
                return "Given user still has subscription";
            }

            int subscriptionId = commandDAO.giveSubscription(giveCommand.getUserId(), giveCommand.getDays());
            return "New subscription with id: '%d' for user: '%s'. Starting now, for %d days".formatted(subscriptionId, user.getName(), giveCommand.getDays());
        }
        return "Unknown command";
    }
}
