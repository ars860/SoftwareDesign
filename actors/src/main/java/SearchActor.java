import akka.actor.AbstractActor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class SearchActor extends AbstractActor {
    private final String searchEngineName;
    private final Function<String, String> apiString;

    public SearchActor(String searchEngineName, Function<String, String> apiString) {
        this.searchEngineName = searchEngineName;
        this.apiString = apiString;
    }

    public static final class Request {
        public final String searchString;

        public Request(String searchString) {
            this.searchString = searchString;
        }
    }

    public static final class Response {
        public final List<SearchEngineResponseRow> res;
        public final String searchEngine;

        public Response(List<SearchEngineResponseRow> res, String searchEngine) {
            this.res = res;
            this.searchEngine = searchEngine;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(Request.class, this::onRequest).build();
    }

    private void onRequest(Request command) {
        try {
            URL url = new URL(apiString.apply(command.searchString));
            String res;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");

            try (InputStream in = connection.getInputStream()) {
                res = new String(in.readAllBytes(), StandardCharsets.UTF_8);

                Gson gson = new Gson();
                List<SearchEngineResponseRow> resParsed = gson.fromJson(res, new TypeToken<List<SearchEngineResponseRow>>() {
                }.getType());

                sender().tell(new Response(resParsed, searchEngineName), self());
            }
        } catch (Exception e) {
            sender().tell(new Response(Collections.emptyList(), searchEngineName), self());
            System.out.println(e.getMessage());
        } finally {
            getContext().stop(self());
        }
    }
}