package manage.command;

import common.DatabaseUtils;

import java.sql.SQLException;
import java.time.LocalDate;

public class CommandDAO {
    public void extendSubscription(int userId, LocalDate due) throws SQLException {
        DatabaseUtils.execSQLNoReturn(
                """
                        insert into Subscriptions (id, user_id, start_date, due_date)
                        values
                            (default, %d, null, '%s');
                        """, userId, DatabaseUtils.formatLocalDate(due)
        );
    }

    public Integer giveSubscription(int userId, int days) throws SQLException {
        LocalDate now = LocalDate.now();
        return DatabaseUtils.execSQLSingle(
                """
                        insert into Subscriptions (id, user_id, start_date, due_date)
                        values
                            (default, %d, '%s', '%s')
                        returning id;
                        """,
                rs -> rs.getInt(1),
                userId,
                DatabaseUtils.formatLocalDate(now),
                DatabaseUtils.formatLocalDate(now.plusDays(days))
        );
    }

    public Integer createUser(String userName) throws SQLException {
        return DatabaseUtils.execSQLSingle(
                """
                        insert into Users (id, name)
                        values
                            (default, '%s')
                        returning id;
                        """, rs -> rs.getInt(1), userName
        );
    }
}
