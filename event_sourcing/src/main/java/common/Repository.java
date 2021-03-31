package common;

import common.domain.Attendance;
import common.domain.Subscription;
import common.domain.User;

import java.sql.SQLException;

public interface Repository {
    Subscription getLastSubscription(int userId) throws SQLException;

    Attendance getLastAttendance(int userId) throws SQLException;

    User getUser(int userId) throws SQLException;
}
