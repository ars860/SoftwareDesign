package manage.query;

import common.DatabaseUtils;
import common.domain.Subscription;

import java.sql.SQLException;
import java.util.List;

public class QueryDAO {
    public List<Subscription> getSubscriptions(int userId) throws SQLException {
        return DatabaseUtils.execSQL(
                """
                        select id, user_id, start_date, due_date from
                        Subscriptions S
                        where
                            S.id >=
                            (select S1.id from
                                Subscriptions S1
                                where
                                    S1.user_id = %d and
                                    S1.start_date is not null
                                order by S1.due_date desc
                                limit 1);
                        """, Subscription::fromResultSet, userId
        );
    }
}
