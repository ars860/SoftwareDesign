package turnstile;

import common.RepositoryImpl;
import common.domain.Attendance;
import spark.Service;
import turnstile.command.CommandDAO;
import turnstile.command.CommandExecutor;
import turnstile.command.model.SaveCommand;
import turnstile.query.QueryDAO;
import turnstile.query.QueryExecutor;
import turnstile.query.model.CheckSubscriptionQuery;

public class Main {
    public static void main(String[] args) {
        Service server = Service.ignite().port(1234);
        CommandExecutor commandExecutor = new CommandExecutor(new CommandDAO(), new RepositoryImpl());
        QueryExecutor queryExecutor = new QueryExecutor(new QueryDAO(new RepositoryImpl()));

        server.post("/enter", (req, res) -> {
            int userId = Integer.parseInt(req.queryParams("user_id"));
            SaveCommand enterCommand = new SaveCommand(Attendance.AttendanceType.ENTER, userId);

            return commandExecutor.execCommand(enterCommand);
        });

        server.post("/exit", (req, res) -> {
            int userId = Integer.parseInt(req.queryParams("user_id"));
            SaveCommand exitCommand = new SaveCommand(Attendance.AttendanceType.EXIT, userId);

            return commandExecutor.execCommand(exitCommand);
        });

        server.get("/check", (req, res) -> {
            int userId = Integer.parseInt(req.queryParams("user_id"));
            CheckSubscriptionQuery query = new CheckSubscriptionQuery(userId);

            return queryExecutor.executeQuery(query);
        });
    }
}
