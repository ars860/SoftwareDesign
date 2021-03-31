import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ControllableClock implements Clock {
    private Instant now;

    public ControllableClock(Instant now) {
        this.now = now;
    }

    public ControllableClock() {
        now = Instant.now();
    }

    @Override
    public Instant now() {
        return now;
    }

    public void modify(int hours, int minutes, int seconds) {
        now = now.plus(hours, ChronoUnit.HOURS).plus(minutes, ChronoUnit.MINUTES).plus(seconds, ChronoUnit.SECONDS);
    }
}
