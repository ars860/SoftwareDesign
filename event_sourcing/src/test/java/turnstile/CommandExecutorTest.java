package turnstile;

import common.Repository;
import common.domain.Attendance;
import common.domain.Subscription;
import common.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import turnstile.command.CommandDAO;
import turnstile.command.CommandExecutor;
import turnstile.command.model.SaveCommand;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommandExecutorTest {
    @Mock
    CommandDAO commandDAO;
    @Mock
    Repository repository;

    @Test
    public void saveCommandTest_VisitingFirstTime() throws SQLException {
        LocalDate now = LocalDate.now();

        when(repository.getUser(anyInt())).thenReturn(new User(1, "test"));
        when(repository.getLastAttendance(anyInt())).thenReturn(null);
        when(repository.getLastSubscription(anyInt())).thenReturn(new Subscription(1, now.minusDays(5), now.plusDays(5)));

        CommandExecutor commandExecutor = new CommandExecutor(commandDAO, repository);
        commandExecutor.execCommand(new SaveCommand(Attendance.AttendanceType.ENTER, 1));

        verify(commandDAO).saveAttendance(1, Attendance.AttendanceType.ENTER);
    }

    @Test
    public void saveCommandTest_VisitingAlreadyIn() throws SQLException {
        LocalDate now = LocalDate.now();

        when(repository.getUser(anyInt())).thenReturn(new User(1, "test"));
        when(repository.getLastAttendance(anyInt())).thenReturn(new Attendance(1, Attendance.AttendanceType.ENTER, LocalDateTime.now().minusHours(1)));
        when(repository.getLastSubscription(anyInt())).thenReturn(new Subscription(1, now.minusDays(5), now.plusDays(5)));

        CommandExecutor commandExecutor = new CommandExecutor(commandDAO, repository);
        assertEquals(
                commandExecutor.execCommand(new SaveCommand(Attendance.AttendanceType.ENTER, 1)),
                "User already entered the gym!"
        );

        verify(commandDAO, never()).saveAttendance(anyInt(), any());
    }

    @Test
    public void saveCommandTest_NoSubscriptionOrExpired() throws SQLException {
        LocalDate now = LocalDate.now();

        when(repository.getUser(anyInt())).thenReturn(new User(1, "test"));
        when(repository.getLastSubscription(anyInt())).thenReturn(null);

        CommandExecutor commandExecutor = new CommandExecutor(commandDAO, repository);
        assertEquals(
                commandExecutor.execCommand(new SaveCommand(Attendance.AttendanceType.ENTER, 1)),
                "No available subscription for user: '1'"
        );

        when(repository.getLastSubscription(anyInt())).thenReturn(new Subscription(1, now.minusDays(5), now.minusDays(4)));
        assertEquals(
                commandExecutor.execCommand(new SaveCommand(Attendance.AttendanceType.ENTER, 1)),
                "No available subscription for user: '1'"
        );

        verify(commandDAO, never()).saveAttendance(anyInt(), any());
    }
}
