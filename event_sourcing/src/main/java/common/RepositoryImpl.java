package common;

import common.domain.Attendance;
import common.domain.Subscription;
import common.domain.User;

import java.sql.SQLException;

public class RepositoryImpl implements Repository {
    @Override
    public Subscription getLastSubscription(int userId) throws SQLException {
        return DatabaseUtils.execSQLSingle(
                """
                        select id, user_id, start_date, due_date from
                            Subscriptions S
                            where
                                S.user_id = %d
                            order by S.due_date desc
                            limit 1;
                        """, Subscription::fromResultSet, userId);
    }

    @Override
    public Attendance getLastAttendance(int userId) throws SQLException {
        return DatabaseUtils.execSQLSingle(
                """
                        select user_id, kind, time_ from
                            Attendances A
                            where
                                A.user_id = %d
                            order by A.time_ desc
                            limit 1;
                        """, Attendance::fromResultSet, userId);
    }

    @Override
    public User getUser(int userId) throws SQLException {
        return DatabaseUtils.execSQLSingle(
                """
                        select id, name from
                            Users U
                            where
                                U.id = %d;
                        """, User::fromResultSet, userId
        );
    }
}
