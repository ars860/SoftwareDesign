package turnstile.query;

import common.Repository;
import common.domain.Subscription;

import java.sql.SQLException;

public class QueryDAO {
    private final Repository repository;

    public QueryDAO(Repository repository) {
        this.repository = repository;
    }

    public Subscription getLastSubscription(int userId) throws SQLException {
         return repository.getLastSubscription(userId);
    }
}
