import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventStatisticsImplTest {
    ControllableClock clock;
    EventStatistics eventStatistics;

    @BeforeEach
    public void before() {
        clock = new ControllableClock();
        eventStatistics = new EventStatisticsImpl(clock);
    }

    @Test
    public void singleEventTest() {
        eventStatistics.incEvent("single");

        assertEquals(Map.of("single", 1 / 60.0), eventStatistics.getAllEventStatistic());
        assertEquals(1 / 60.0, eventStatistics.getEventStatisticByName("single"));
    }

    @Test
    public void someEventsTest() {
        eventStatistics.incEvent("a");
        eventStatistics.incEvent("a");
        eventStatistics.incEvent("b");
        eventStatistics.incEvent("b");
        eventStatistics.incEvent("b");
        eventStatistics.incEvent("b");
        eventStatistics.incEvent("c");

        assertEquals(Map.ofEntries(entry("a", 2 / 60.0), entry("b", 4 / 60.0), entry("c", 1 / 60.0)),
                eventStatistics.getAllEventStatistic());
    }

    @Test
    public void oldEventsRemovedTest() {
        eventStatistics.incEvent("old");

        assertEquals(Map.ofEntries(entry("old", 1 / 60.0)), eventStatistics.getAllEventStatistic());

        clock.modify(1, 0, 0);

        assertEquals(Map.ofEntries(entry("old", 0.0)), eventStatistics.getAllEventStatistic());
    }

    @Test
    public void oldEventsPartiallyRemovedTest() {
        eventStatistics.incEvent("old");

        clock.modify(0, 0, 30);
        eventStatistics.incEvent("old");
        eventStatistics.incEvent("old");

        assertEquals(Map.ofEntries(entry("old", 3 / 60.0)), eventStatistics.getAllEventStatistic());

        clock.modify(0, 59, 30);

        assertEquals(Map.ofEntries(entry("old", 2 / 60.0)), eventStatistics.getAllEventStatistic());

        clock.modify(0, 0, 30);

        assertEquals(Map.ofEntries(entry("old", 0.0)), eventStatistics.getAllEventStatistic());
    }
}
