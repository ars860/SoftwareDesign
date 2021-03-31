package turnstile.command;

import common.DatabaseUtils;
import common.domain.Attendance;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class CommandDAO {
    public void saveAttendance(int userId, Attendance.AttendanceType kind) throws SQLException {
        DatabaseUtils.execSQLNoReturn(
                """
                    insert into Attendances (user_id, kind, time_)
                    values
                        (%d, '%s', '%s');
                    """, userId, kind.show(), DatabaseUtils.formatLocalDateTime(LocalDateTime.now())
        );
    }
}
