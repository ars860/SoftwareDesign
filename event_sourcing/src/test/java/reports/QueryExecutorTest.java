package reports;

import common.Repository;
import common.Utils;
import common.domain.Attendance;
import common.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reports.query.QueryDAO;
import reports.query.QueryExecutor;
import reports.query.model.DurationAndPeriodicityQuery;
import reports.query.model.Query;
import reports.query.model.StatsByDayQuery;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QueryExecutorTest {
    @Mock
    QueryDAO queryDAO;
    @Mock
    Repository repository;

    QueryExecutor queryExecutor;

    LocalDateTime now;

    @BeforeEach
    public void beforeEach() {
        queryExecutor = new QueryExecutor(queryDAO, repository);
        now = LocalDateTime.now();
    }

    @Test
    public void DurationAndPeriodicityQueryTest() throws SQLException {
        when(repository.getUser(anyInt())).thenReturn(new User(1, "test"));
        when(queryDAO.getAllAttendancesByUser(anyInt())).thenReturn(
                List.of(
                        new Attendance(1, Attendance.AttendanceType.ENTER, now.minusDays(3)),
                        new Attendance(1, Attendance.AttendanceType.EXIT, now.minusDays(3).plusHours(1)),
                        new Attendance(1, Attendance.AttendanceType.ENTER, now.minusDays(2)),
                        new Attendance(1, Attendance.AttendanceType.EXIT, now.minusDays(2).plusHours(2)),
                        new Attendance(1, Attendance.AttendanceType.ENTER, now.minusDays(1)),
                        new Attendance(1, Attendance.AttendanceType.EXIT, now.minusDays(1).plusHours(3))
                )
        );

        Query query = new DurationAndPeriodicityQuery(1);

        assertEquals(
                queryExecutor.executeQuery(query),
                "Mean duration: '%s', mean interval between visits: '%s'.".formatted(
                        Utils.formatDuration(Duration.ofHours(2)),
                        Utils.formatDuration(Duration.ofDays(1))
                )
        );
    }

    @Test
    public void StatsByDayQueryTest() throws SQLException {
        when(repository.getUser(anyInt())).thenReturn(new User(1, "test"));
        when(queryDAO.getAllEnterAttendancesByUser(anyInt())).thenReturn(
                List.of(
                        new Attendance(1, Attendance.AttendanceType.ENTER, now.minusDays(3)),
                        new Attendance(1, Attendance.AttendanceType.ENTER, now.minusDays(2)),
                        new Attendance(1, Attendance.AttendanceType.ENTER, now.minusDays(1))
                )
        );

        Query query = new StatsByDayQuery(1);

        List<String> expected = List.of(
                now.minusDays(1).getDayOfWeek(),
                now.minusDays(2).getDayOfWeek(),
                now.minusDays(3).getDayOfWeek()
        ).stream().map(day -> "%s: 1 visits, 33,33 percents".formatted(day.toString())).collect(Collectors.toList());
        expected.add("Stats by days of the week:");
        List<String> actual = Arrays.asList(queryExecutor.executeQuery(query).split("\n").clone());

        assertTrue(expected.size() == actual.size() && expected.containsAll(actual) && expected.containsAll(actual));
    }
}
