package turnstile.query;

import common.DatabaseUtils;
import common.domain.Subscription;
import turnstile.query.model.CheckSubscriptionQuery;
import turnstile.query.model.Query;

import java.sql.SQLException;
import java.time.LocalDate;

public class QueryExecutor {
    private final QueryDAO queryDAO;

    public QueryExecutor(QueryDAO queryDAO) {
        this.queryDAO = queryDAO;
    }

    public String executeQuery(Query query) throws SQLException {
        if (query instanceof CheckSubscriptionQuery) {
            CheckSubscriptionQuery checkQuery = (CheckSubscriptionQuery) query;
            Subscription subscription = queryDAO.getLastSubscription(checkQuery.getUserId());

            if (subscription == null || subscription.getDueDate().isBefore(LocalDate.now())) {
                return "No available subscription for user: '%d'".formatted(checkQuery.getUserId());
            }

            return "Subscription available until '%s'".formatted(DatabaseUtils.formatLocalDate(subscription.getDueDate()));
        }

        return "Unknown query";
    }
}
