package manage;

import common.DatabaseUtils;
import common.Repository;
import common.domain.Subscription;
import common.domain.User;
import manage.query.QueryExecutor;
import manage.query.model.GetSubscriptionQuery;
import manage.query.model.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import manage.query.QueryDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QueryExecutorTest {
    @Mock
    QueryDAO queryDAO;
    @Mock
    Repository repository;

    @Test
    public void getSubscriptionQuery_addUpTest() throws SQLException {
        LocalDate now = LocalDate.now();
        when(queryDAO.getSubscriptions(anyInt())).thenReturn(
                List.of(
                        new Subscription(1, now.minusDays(5), now.minusDays(3)),
                        new Subscription(1, null, now),
                        new Subscription(1, null, now.plusDays(3))
                )
        );
        when(repository.getUser(anyInt())).thenReturn(new User(1, "test"));

        QueryExecutor queryExecutor = new QueryExecutor(queryDAO, repository);

        Query query = new GetSubscriptionQuery(1);

        assertEquals(
                queryExecutor.executeQuery(query),
                "Current subscription. Active from '%s' to '%s'. Extended %d times".formatted(
                        DatabaseUtils.formatLocalDate(now.minusDays(5)),
                        DatabaseUtils.formatLocalDate(now.plusDays(3)),
                        2
                )
        );
    }

    @Test
    public void getSubscriptionQuery_noUserTest() throws SQLException {
        LocalDate now = LocalDate.now();
        when(repository.getUser(anyInt())).thenReturn(null);

        QueryExecutor queryExecutor = new QueryExecutor(queryDAO, repository);

        Query query = new GetSubscriptionQuery(1);

        assertEquals(
                queryExecutor.executeQuery(query),
                "No such user"
        );
    }

    @Test
    public void getSubscriptionQuery_noSubscriptionTest() throws SQLException {
        when(queryDAO.getSubscriptions(anyInt())).thenReturn(
                Collections.emptyList()
        );
        when(repository.getUser(anyInt())).thenReturn(new User(1, "test"));

        QueryExecutor queryExecutor = new QueryExecutor(queryDAO, repository);

        Query query = new GetSubscriptionQuery(1);

        assertEquals(
                queryExecutor.executeQuery(query),
                "No subscriptions for this user"
        );
    }
}
