import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class SearchActorOrganizer extends AbstractActor {
    private CompletableFuture<Map<String, List<SearchEngineResponseRow>>> res;
    private final Map<String, List<SearchEngineResponseRow>> stored = new HashMap<>();

    public static final class Request {
        public final String searchString;
        public final CompletableFuture<Map<String, List<SearchEngineResponseRow>>> result;

        public Request(String searchString, CompletableFuture<Map<String, List<SearchEngineResponseRow>>> result) {
            this.searchString = searchString;
            this.result = result;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SearchActorOrganizer.Request.class, this::onRequest)
                .match(SearchActor.Response.class, this::onResponse)
                .match(ReceiveTimeout.class, this::onTimeout)
                .build();
    }

    private void onRequest(SearchActorOrganizer.Request command) {
        List<ActorRef> children = List.of(
                getContext().actorOf(Props.create(SearchActor.class, "Google", (Function<String, String>) (s -> "http://localhost:1234/search?q=" + s)), "googleSearcher"),
                getContext().actorOf(Props.create(SearchActor.class, "Yandex", (Function<String, String>) (s -> "http://localhost:1235/search?q=" + s)), "yandexSearcher"),
                getContext().actorOf(Props.create(SearchActor.class, "Yahoo", (Function<String, String>) (s -> "http://localhost:1236/search?q=" + s)), "yahooSearcher")
        );

        for (ActorRef child : children) {
            child.tell(new SearchActor.Request(command.searchString), self());
        }

        getContext().setReceiveTimeout(Duration.ofSeconds(1));
        res = command.result;
    }

    private void onResponse(SearchActor.Response command) {
        stored.put(command.searchEngine, command.res);

        if (stored.size() == 3) {
            res.complete(stored);

            getContext().cancelReceiveTimeout();
            getContext().stop(self());
        }
    }

    private void onTimeout(ReceiveTimeout command) {
        res.complete(stored);

        getContext().cancelReceiveTimeout();
        getContext().stop(self());
    }
}
