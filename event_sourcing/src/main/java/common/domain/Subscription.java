package common.domain;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Subscription {
    int userId;
    LocalDate startDate;
    LocalDate dueDate;

    public Subscription(int userId, LocalDate startDate, LocalDate dueDate) {
        this.userId = userId;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    public static Subscription fromResultSet(ResultSet resultSet) throws SQLException {
        Date startDate = resultSet.getDate("start_date");

        return new Subscription(resultSet.getInt("user_id"), startDate == null ? null : startDate.toLocalDate(), resultSet.getDate("due_date").toLocalDate());
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
