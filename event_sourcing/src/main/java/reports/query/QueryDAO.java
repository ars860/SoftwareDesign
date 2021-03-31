package reports.query;

import common.DatabaseUtils;
import common.domain.Attendance;
import reports.query.model.DurationAndPeriodicityQuery;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class QueryDAO {
    public List<Attendance> getAllAttendancesByUser(int userId) throws SQLException {
        return DatabaseUtils.execSQL(
                """
                        select user_id, kind, time_ from
                        Attendances A
                        where
                            A.user_id = %d
                            """, Attendance::fromResultSet, userId
        );
    }

    public List<Attendance> getAllEnterAttendancesByUser(int userId) throws SQLException {
        return DatabaseUtils.execSQL(
                """
                        select user_id, kind, time_ from
                        Attendances A
                        where
                            A.user_id = %d and
                            A.kind = 'enter'
                            """, Attendance::fromResultSet, userId
        );
    }
}
