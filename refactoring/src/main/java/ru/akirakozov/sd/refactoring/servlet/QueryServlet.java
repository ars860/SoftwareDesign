package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.service.ResponseService;
import ru.akirakozov.sd.refactoring.service.ProductService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        switch (command) {
            case "max": {
                Product maxPriceProduct = ProductService.getMax();

                ResponseService.writeResponseList(Collections.singletonList(maxPriceProduct),
                        "<h1>Product with max price: </h1>",
                        product -> product.getName() + "\t" + product.getPrice() + "</br>",
                        response);
                break;
            }
            case "min": {
                Product minPriceProduct = ProductService.getMin();

                ResponseService.writeResponseList(Collections.singletonList(minPriceProduct),
                        "<h1>Product with min price: </h1>",
                        product -> product.getName() + "\t" + product.getPrice() + "</br>",
                        response);
                break;
            }
            case "sum": {
                Long sum = ProductService.getSum();

                ResponseService.writeResponseList(Collections.singletonList(sum),
                        "Summary price: ",
                        Object::toString,
                        response);
                break;
            }
            case "count": {
                Long sum = ProductService.getCount();

                ResponseService.writeResponseList(Collections.singletonList(sum),
                        "Number of products: ",
                        Object::toString,
                        response);
                break;
            }
            default: {
                response.getWriter().println("Unknown command: " + command);
            }
        }
    }

}
