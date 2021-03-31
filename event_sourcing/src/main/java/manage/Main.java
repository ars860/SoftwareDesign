package manage;

import common.Repository;
import common.RepositoryImpl;
import manage.command.CommandDAO;
import manage.command.CommandExecutor;
import manage.command.model.CreateUserCommand;
import manage.command.model.ExtendCommand;
import manage.command.model.GiveCommand;
import manage.query.QueryDAO;
import manage.query.QueryExecutor;
import manage.query.model.GetSubscriptionQuery;
import spark.Service;

public class Main {
    public static void main(String[] args) {
        Service server = Service.ignite().port(1235);
        Repository repository = new RepositoryImpl();
        CommandExecutor commandExecutor = new CommandExecutor(new CommandDAO(), repository);
        QueryExecutor queryExecutor = new QueryExecutor(new QueryDAO(), repository);

        server.post("/create_user", (req, res) -> {
            String name = req.queryParams("name");
            CreateUserCommand command = new CreateUserCommand(name);

            return commandExecutor.executeCommand(command);
        });

        server.post("/extend_subscription", (req, res) -> {
            int userId = Integer.parseInt(req.queryParams("user_id"));
            int days = Integer.parseInt(req.queryParams("days"));
            ExtendCommand command = new ExtendCommand(userId, days);

            return commandExecutor.executeCommand(command);
        });

        server.post("/give_subscription", (req, res) -> {
            int userId = Integer.parseInt(req.queryParams("user_id"));
            int days = Integer.parseInt(req.queryParams("days"));
            GiveCommand command = new GiveCommand(userId, days);

            return commandExecutor.executeCommand(command);
        });

        server.get("/subscription", (req, res) -> {
            int userId = Integer.parseInt(req.queryParams("user_id"));
            GetSubscriptionQuery query = new GetSubscriptionQuery(userId);

            return queryExecutor.executeQuery(query);
        });
    }
}
