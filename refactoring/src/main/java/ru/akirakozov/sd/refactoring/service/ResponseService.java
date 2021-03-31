package ru.akirakozov.sd.refactoring.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class ResponseService {
    static public <T> void writeResponseList(List<T> thingsToWrite, String header,
                                             Function<T, String> thing2string,
                                             HttpServletResponse response) throws IOException {
        response.getWriter().println("<html><body>");

        if (header != null) {
            response.getWriter().println(header);
        }

        for (T thing : thingsToWrite) {
            response.getWriter().println(thing2string.apply(thing));
        }

        response.getWriter().println("</body></html>");
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    static public void writeResponseSingleMessage(String message, HttpServletResponse response) throws IOException {
        response.getWriter().println(message);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
