package common;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {
    private static final String databaseURL = "jdbc:postgresql://localhost:5432/ppo_gym";
    private static final String databaseUser = "postgres";
    private static final String databasePassword = "1";

    @FunctionalInterface
    public interface ConverterFunction<T> {
        T convert(ResultSet s) throws SQLException;
    }

    public static <T> List<T> execSQL(String sql, ConverterFunction<T> converter, Object... params) throws SQLException {
        try (
                Connection c = DriverManager.getConnection(databaseURL, databaseUser, databasePassword);
                Statement s = c.createStatement();
        ) {
            s.execute(String.format(sql, params));
            ResultSet resultSet = s.getResultSet();

            List<T> result = new ArrayList<>();
            if (converter != null) {
                while (resultSet.next()) {
                    result.add(converter.convert(resultSet));
                }
            }

            return result;
        }
    }

    public static <T> T execSQLSingle(String sql, ConverterFunction<T> converter, Object... params) throws SQLException {
        List<T> resultList = execSQL(sql, converter, params);
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    public static <T> void execSQLNoReturn(String sql, Object... params) throws SQLException {
        execSQL(sql, null, params);
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
    }

    public static String formatLocalDate(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
