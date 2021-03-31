import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class EventStatisticsImpl implements EventStatistics {
    private final Clock clock;
    private final Map<String, Queue<Instant>> event2instants;

    public EventStatisticsImpl(Clock clock) {
        this.clock = clock;
        event2instants = new HashMap<>();
    }

    private void removeOld() {
        Instant now = clock.now();
        Instant oneHourBack = now.minus(1, ChronoUnit.HOURS);

        for (Map.Entry<String, Queue<Instant>> name2instants : event2instants.entrySet()) {
            Queue<Instant> instants = name2instants.getValue();

            while (instants.peek() != null && instants.peek().compareTo(oneHourBack) <= 0) {
                instants.poll();
            }
        }
    }

    @Override
    public void incEvent(String name) {
        removeOld();

        Instant now = clock.now();
        event2instants.computeIfAbsent(name, __ -> new LinkedList<>()).add(now);
    }

    @Override
    public double getEventStatisticByName(String name) {
        removeOld();

        int eventCnt = event2instants.get(name).size();

        return eventCnt / 60.0;
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        removeOld();

        return event2instants.entrySet()
                .stream()
                .map(e2i -> entry(e2i.getKey(), e2i.getValue().size() / 60.0))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void printStatistic() {
        removeOld();

        Map<String, Double> stat = getAllEventStatistic();

        System.out.println("Events for last hour:");
        for (Map.Entry<String, Double> name2instants : stat.entrySet()) {
            System.out.println("Event: " + name2instants.getKey() + " with rpm: " + name2instants.getValue());
        }
    }
}
