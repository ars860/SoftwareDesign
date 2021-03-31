import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.gson.Gson;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.Delay;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class SearchActorTest {
    private static class HelperActor extends AbstractActor {
        public final CompletableFuture<List<SearchEngineResponseRow>> result;

        public HelperActor(CompletableFuture<List<SearchEngineResponseRow>> result) {
            this.result = result;
        }

        public static class Start {}

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(Start.class, this::onStart)
                    .match(SearchActor.Response.class, this::onFinish)
                    .build();
        }

        public void onStart(Start start) {
            ActorRef searchActor = getContext().actorOf(Props.create(SearchActor.class, () -> new SearchActor("abacaba", s -> "http://localhost:1111/search?q=" + s)));
            searchActor.tell(new SearchActor.Request("test"), self());
        }

        public void onFinish(SearchActor.Response response) {
            result.complete(response.res);
        }
    }

    private MockServerClient mockServer;

    private ActorSystem system;

    private final Gson gson = new Gson();

    private List<SearchEngineResponseRow> response =
            List.of(
                    new SearchEngineResponseRow("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tem",
                            "https://lorem_ipsum.dolor.sit.amet",
                            "Accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis"),
                    new SearchEngineResponseRow("1", "2", "3"),
                    new SearchEngineResponseRow("aba", "caba", "baba")
            );
    private Delay delay = Delay.milliseconds(30);

    @BeforeEach
    public void beforeEach() {
        startClientAndServer(1111);

        mockServer = new MockServerClient("localhost", 1111);

        system = ActorSystem.create();
    }

    @AfterEach
    public void afterEach() {
        mockServer.stop();
    }

    @Test
    public void responseTest() throws ExecutionException, InterruptedException {
        mockServer.when(
                request().withMethod("GET")
        ).respond(
                response()
                        .withDelay(delay)
                        .withStatusCode(200)
                        .withBody(gson.toJson(response))
        );

        CompletableFuture<List<SearchEngineResponseRow>> result = new CompletableFuture<>();
        ActorRef searchActor = system.actorOf(Props.create(HelperActor.class, () -> new HelperActor(result)));
        searchActor.tell(new HelperActor.Start(), ActorRef.noSender());

        List<SearchEngineResponseRow> resultCalculated = result.get();

        assertThat(resultCalculated, IsIterableContainingInOrder.contains(response.toArray()));
    }

    @Test
    public void noResponseTest() throws ExecutionException, InterruptedException {
        CompletableFuture<List<SearchEngineResponseRow>> result = new CompletableFuture<>();
        ActorRef searchActor = system.actorOf(Props.create(HelperActor.class, () -> new HelperActor(result)));
        searchActor.tell(new HelperActor.Start(), ActorRef.noSender());

        List<SearchEngineResponseRow> resultCalculated = result.get();

        assertEquals(resultCalculated, Collections.emptyList());
    }
}
