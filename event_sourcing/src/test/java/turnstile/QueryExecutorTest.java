package turnstile;

import common.DatabaseUtils;
import common.domain.Subscription;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import turnstile.query.QueryDAO;
import turnstile.query.QueryExecutor;
import turnstile.query.model.CheckSubscriptionQuery;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QueryExecutorTest {
    @Test
    public void checkSubscriptionQuery_HasSubscriptionTest() throws SQLException {
        QueryDAO queryDAO = mock(QueryDAO.class);

        LocalDate now = LocalDate.now();
        Subscription lastSubscription = new Subscription(1, now.minusDays(5), now.plusDays(5));
        when(queryDAO.getLastSubscription(anyInt())).thenReturn(lastSubscription);

        QueryExecutor queryExecutor = new QueryExecutor(queryDAO);

        assertEquals(
                queryExecutor.executeQuery(new CheckSubscriptionQuery(1)),
                "Subscription available until '%s'".formatted(DatabaseUtils.formatLocalDate(now.plusDays(5)))
        );
    }

    @Test
    public void checkSubscriptionQuery_NoSubscriptionTest() throws SQLException {
        QueryDAO queryDAO = mock(QueryDAO.class);

        when(queryDAO.getLastSubscription(anyInt())).thenReturn(null);

        QueryExecutor queryExecutor = new QueryExecutor(queryDAO);

        assertEquals(
                queryExecutor.executeQuery(new CheckSubscriptionQuery(1)),
                "No available subscription for user: '1'"
        );
    }
}
