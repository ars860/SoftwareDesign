package turnstile.command.model;

import common.domain.Attendance;

public class SaveCommand implements Command {
    Attendance.AttendanceType kind;
    int userId;

    public SaveCommand(Attendance.AttendanceType kind, int userId) {
        this.kind = kind;
        this.userId = userId;
    }

    public Attendance.AttendanceType getKind() {
        return kind;
    }

    public void setKind(Attendance.AttendanceType kind) {
        this.kind = kind;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
