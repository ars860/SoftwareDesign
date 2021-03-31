import java.util.Map;

public interface EventStatistics {
    void incEvent(String name);
    double getEventStatisticByName(String name);
    Map<String, Double> getAllEventStatistic();
    void printStatistic();
}
