package reports;

import common.RepositoryImpl;
import common.Utils;
import reports.query.QueryDAO;
import reports.query.QueryExecutor;
import reports.query.model.DurationAndPeriodicityQuery;
import reports.query.model.StatsByDayQuery;
import spark.Service;

public class Main {
    public static void main(String[] args) {
        Service server = Service.ignite().port(1236);
        QueryExecutor queryExecutor = new QueryExecutor(new QueryDAO(), new RepositoryImpl());

        server.get("/d_p_stats", (req, res) -> {
            int userId = Integer.parseInt(Utils.queryParams(req, "user_id"));
            DurationAndPeriodicityQuery query = new DurationAndPeriodicityQuery(userId);

            return queryExecutor.executeQuery(query);
        });

        server.get("/day_stats", (req, res) -> {
            int userId = Integer.parseInt(Utils.queryParams(req, "user_id"));
            StatsByDayQuery query = new StatsByDayQuery(userId);

            return queryExecutor.executeQuery(query);
        });
    }
}
