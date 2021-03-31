package ru.akirakozov.sd.refactoring.service;

import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.repository.ProductRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class ProductService {
    static private <T> T checkEmptyAndGet(int index, List<T> list) {
        if (index < list.size()) {
            return list.get(index);
        }

        return null;
    }

    static public Product getMax() {
        List<Product> queryExecuted = ProductRepository.execQueryProducts("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");

        return checkEmptyAndGet(0, queryExecuted);
    }

    static public Product getMin() {
        List<Product> queryExecuted = ProductRepository.execQueryProducts("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");

        return checkEmptyAndGet(0, queryExecuted);
    }

    static public Long getSum() {
        List<Long> queryExecuted = ProductRepository.execQueryLong("SELECT SUM(price) FROM PRODUCT");

        return checkEmptyAndGet(0, queryExecuted);
    }

    static public Long getCount() {
        List<Long> queryExecuted = ProductRepository.execQueryLong("SELECT COUNT(*) FROM PRODUCT");

        return checkEmptyAndGet(0, queryExecuted);
    }

    static public List<Product> getAll() {
        return ProductRepository.execQueryProducts("SELECT * FROM PRODUCT");
    }

    static public void insertProduct(Product product) {
        String sql = "INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + product.getName() + "\"," + product.getPrice() + ")";

        ProductRepository.executeUpdate(sql);
    }

}
