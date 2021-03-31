package common.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    int id;
    String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static User fromResultSet(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getInt("id"), resultSet.getString("name"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
