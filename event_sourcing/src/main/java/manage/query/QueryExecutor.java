package manage.query;

import common.DatabaseUtils;
import common.Repository;
import common.domain.Subscription;
import common.domain.User;
import manage.query.model.GetSubscriptionQuery;
import manage.query.model.Query;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class QueryExecutor {
    private final QueryDAO queryDAO;
    private final Repository repository;

    public QueryExecutor(QueryDAO queryDAO, Repository repository) {
        this.queryDAO = queryDAO;
        this.repository = repository;
    }

    public String executeQuery(Query query) throws SQLException {
        if (query instanceof GetSubscriptionQuery) {
            GetSubscriptionQuery getSubscriptionQuery = (GetSubscriptionQuery) query;

            User user = repository.getUser(getSubscriptionQuery.getUserId());
            if (user == null) {
                return "No such user";
            }

            List<Subscription> subscriptions = queryDAO.getSubscriptions(getSubscriptionQuery.getUserId());
            if (subscriptions.isEmpty()) {
                return "No subscriptions for this user";
            }

            LocalDate fromDate = subscriptions.get(0).getStartDate();
            LocalDate dueDate = subscriptions.get(subscriptions.size() - 1).getDueDate();

            return "%s subscription. Active from '%s' to '%s'. Extended %d times"
                    .formatted(
                            dueDate.isBefore(LocalDate.now()) ? "Previous" : "Current",
                            DatabaseUtils.formatLocalDate(fromDate),
                            DatabaseUtils.formatLocalDate(dueDate),
                            subscriptions.size() - 1
                    );
        }
        return "Unknown query";
    }
}
