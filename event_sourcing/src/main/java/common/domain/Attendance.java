package common.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Attendance {
    public enum AttendanceType {
        ENTER("enter"), EXIT("exit");

        private final String show;

        AttendanceType(String show) {
            this.show = show;
        }

        public String show() {
            return show;
        }

        public static AttendanceType fromString(String s) {
            if (s.equals("enter")) {
                return ENTER;
            }
            return EXIT;
        }
    }

    int userId;
    AttendanceType kind;
    LocalDateTime time;

    public Attendance(int userId, AttendanceType kind, LocalDateTime time) {
        this.userId = userId;
        this.kind = kind;
        this.time = time;
    }

    public static Attendance fromResultSet(ResultSet resultSet) throws SQLException {
        return new Attendance(resultSet.getInt("user_id"), AttendanceType.fromString(resultSet.getString("kind")), resultSet.getTimestamp("time_").toLocalDateTime());
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public AttendanceType getKind() {
        return kind;
    }

    public void setKind(AttendanceType kind) {
        this.kind = kind;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
