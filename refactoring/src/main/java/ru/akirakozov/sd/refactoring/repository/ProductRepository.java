package ru.akirakozov.sd.refactoring.repository;

import ru.akirakozov.sd.refactoring.domain.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private interface ResultSetMapper<T> {
        T apply(ResultSet rs) throws SQLException;
    }

    static public void initRepository() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    static public List<Product> execQueryProducts(String query) {
        return execQueryT(query, rs -> {
            String name = rs.getString("name");
            long price = rs.getLong("price");

            return new Product(name, price);
        });
    }

    static public List<Long> execQueryLong(String query) {
        return execQueryT(query, rs -> rs.getLong(1));
    }

    static private <T> List<T> execQueryT(String query, ResultSetMapper<T> mapper) {
        try {
            List<T> result = new ArrayList<>();
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    result.add(mapper.apply(rs));
                }

                rs.close();
                stmt.close();
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static public void executeUpdate(String sql) {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
