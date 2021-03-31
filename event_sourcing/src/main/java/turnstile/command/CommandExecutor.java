package turnstile.command;

import common.Repository;
import common.domain.Attendance;
import common.domain.Subscription;
import common.domain.User;
import turnstile.command.model.Command;
import turnstile.command.model.SaveCommand;

import java.sql.SQLException;
import java.time.LocalDate;

public class CommandExecutor {
    private final CommandDAO commandDAO;
    private final Repository repository;

    public CommandExecutor(CommandDAO commandDAO, Repository repository) {
        this.commandDAO = commandDAO;
        this.repository = repository;
    }

    public String execCommand(Command command) throws SQLException {
        if (command instanceof SaveCommand) {
            SaveCommand saveCommand = (SaveCommand) command;
            User user = repository.getUser(saveCommand.getUserId());

            if (user == null) {
                return "No such user!";
            }

            Subscription subscription = repository.getLastSubscription(saveCommand.getUserId());

            if (saveCommand.getKind() == Attendance.AttendanceType.ENTER &&
                    (subscription == null || subscription.getDueDate().isBefore(LocalDate.now()))) {
                return "No available subscription for user: '%d'".formatted(saveCommand.getUserId());
            }

            Attendance prev = repository.getLastAttendance(saveCommand.getUserId());

            if (prev != null && prev.getKind() == saveCommand.getKind()) {
                if (prev.getKind() == Attendance.AttendanceType.ENTER) {
                    return "User already entered the gym!";
                } else {
                    return "User already exited the gym!";
                }
            } else {
                if (prev == null && saveCommand.getKind() == Attendance.AttendanceType.EXIT) {
                    return "User is not currently at the gym!";
                }
            }

            commandDAO.saveAttendance(saveCommand.getUserId(), saveCommand.getKind());
            return "Success: user '%s' successfully %s the gym!".formatted(user.getName(), saveCommand.getKind() == Attendance.AttendanceType.ENTER ? "entered" : "exits");
        }
        return "Unknown command!";
    }
}
