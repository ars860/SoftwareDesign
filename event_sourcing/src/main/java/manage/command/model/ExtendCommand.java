package manage.command.model;

import manage.command.model.Command;

import java.time.LocalDate;

public class ExtendCommand implements Command {
    int userId;
    int days;

    public ExtendCommand(int userId, int days) {
        this.userId = userId;
        this.days = days;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
