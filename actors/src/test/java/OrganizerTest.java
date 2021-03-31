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

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class OrganizerTest {
    private MockServerClient googleServer;
    private MockServerClient yandexServer;
    private MockServerClient yahooServer;

    private ActorSystem system;
    private ActorRef organizer;

    private final Gson gson = new Gson();

    @BeforeEach
    public void beforeEach() {
        startClientAndServer(1234);
        startClientAndServer(1235);
        startClientAndServer(1236);

        googleServer = new MockServerClient("localhost", 1234);
        yandexServer = new MockServerClient("localhost", 1235);
        yahooServer = new MockServerClient("localhost", 1236);

        system = ActorSystem.create();
        organizer = system.actorOf(Props.create(SearchActorOrganizer.class));
    }

    @AfterEach
    public void afterEach() {
        googleServer.stop();
        yandexServer.stop();
        yahooServer.stop();
    }

    @Test
    public void simpleTest() throws ExecutionException, InterruptedException {
        List<SearchEngineResponseRow> googleResponse =
                List.of(
                        new SearchEngineResponseRow("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tem",
                                "https://lorem_ipsum.dolor.sit.amet",
                                "Accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis"),
                        new SearchEngineResponseRow("1", "2", "3"),
                        new SearchEngineResponseRow("aba", "caba", "baba")
                );
        Delay googleDelay = Delay.milliseconds(30);

        List<SearchEngineResponseRow> yandexResponse =
                List.of(
                        new SearchEngineResponseRow("Install Yandex browser!!!", "https://download_yandex_browser.ru", "InstallInstallInstallInstall"),
                        new SearchEngineResponseRow("1", "2", "3"),
                        new SearchEngineResponseRow("aba", "caba", "baba")
                );
        Delay yandexDelay = Delay.milliseconds(300);

        Delay yahooDelay = Delay.seconds(3);

        googleServer.when(
                request().withMethod("GET")
        ).respond(
                response()
                        .withDelay(googleDelay)
                        .withStatusCode(200)
                        .withBody(gson.toJson(googleResponse))
        );

        yandexServer.when(
                request().withMethod("GET")
        ).respond(
                response()
                        .withDelay(yandexDelay)
                        .withStatusCode(200)
                        .withBody(gson.toJson(yandexResponse))
        );

        yahooServer.when(
                request().withMethod("GET")
        ).respond(
                response()
                        .withDelay(yahooDelay)
                        .withStatusCode(200)
                        .withBody(gson.toJson(List.of()))
        );

        CompletableFuture<Map<String, List<SearchEngineResponseRow>>> result = new CompletableFuture<>();
        organizer.tell(new SearchActorOrganizer.Request("test", result), ActorRef.noSender());
        var resultComputed = result.get();

        var googleResult = resultComputed.get("Google");
        var yandexResult = resultComputed.get("Yandex");
        var yahooResult = resultComputed.get("Yahoo");

        assertNotNull(googleResult);
        assertNotNull(yandexResult);
        assertNull(yahooResult);

        assertThat(googleResult, IsIterableContainingInOrder.contains(googleResponse.toArray()));
        assertThat(yandexResult, IsIterableContainingInOrder.contains(yandexResponse.toArray()));
    }
}
