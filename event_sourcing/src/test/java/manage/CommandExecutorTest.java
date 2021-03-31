package manage;

import common.DatabaseUtils;
import common.Repository;
import common.domain.Subscription;
import common.domain.User;
import manage.command.CommandDAO;
import manage.command.CommandExecutor;
import manage.command.model.Command;
import manage.command.model.CreateUserCommand;
import manage.command.model.ExtendCommand;
import manage.command.model.GiveCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommandExecutorTest {
    @Mock
    CommandDAO commandDAO;
    @Mock
    Repository repository;

    CommandExecutor commandExecutor;

    LocalDate now;

    @BeforeEach
    public void beforeEach() {
        now = LocalDate.now();
        commandExecutor = new CommandExecutor(commandDAO, repository);
    }

    @Test
    public void CreateUserCommandTest() throws SQLException {
        when(commandDAO.createUser(anyString())).thenReturn(1);

        Command command = new CreateUserCommand("test");

        assertEquals(commandExecutor.executeCommand(command), "User 'test' with id '1' created");
        verify(commandDAO).createUser("test");
    }

    @Test
    public void ExtendCommandTest_workingTest() throws SQLException {
        when(repository.getUser(anyInt())).thenReturn(new User(1, "test"));
        when(repository.getLastSubscription(anyInt())).thenReturn(new Subscription(1, now.minusDays(5), now.plusDays(5)));

        Command command = new ExtendCommand(1, 5);

        assertEquals(
                commandExecutor.executeCommand(command),
                "Subscription for user: 'test' extended until '%s'".formatted(DatabaseUtils.formatLocalDate(now.plusDays(5).plusDays(5)))
        );
        verify(commandDAO).extendSubscription(1, now.plusDays(5).plusDays(5));
    }

    @Test
    public void ExtendCommandTest_noUserTest() throws SQLException {
        when(repository.getUser(anyInt())).thenReturn(null);

        Command command = new ExtendCommand(1, 5);

        assertEquals(
                commandExecutor.executeCommand(command),
                "No such user!"
        );
        verify(commandDAO, never()).extendSubscription(anyInt(), any());
    }

    @Test
    public void ExtendCommandTest_noSubscriptionOrExpiredTest() throws SQLException {
        when(repository.getUser(anyInt())).thenReturn(new User(1, "test"));
        when(repository.getLastSubscription(anyInt())).thenReturn(new Subscription(1, now.minusDays(5), now.minusDays(3)));

        Command command = new ExtendCommand(1, 5);

        assertEquals(
                commandExecutor.executeCommand(command),
                "No active subscription for user: '1'"
        );

        when(repository.getLastSubscription(anyInt())).thenReturn(null);

        assertEquals(
                commandExecutor.executeCommand(command),
                "No active subscription for user: '1'"
        );

        verify(commandDAO, never()).extendSubscription(1, now.plusDays(5).plusDays(5));
    }

    @Test
    public void GiveCommandTest_workingTest() throws SQLException {
        when(repository.getUser(anyInt())).thenReturn(new User(1, "test"));
        when(repository.getLastSubscription(anyInt())).thenReturn(null);

        when(commandDAO.giveSubscription(anyInt(), anyInt())).thenReturn(1);

        Command command = new GiveCommand(1, 5);

        assertEquals(
                commandExecutor.executeCommand(command),
                "New subscription with id: '%d' for user: '%s'. Starting now, for %d days"
                        .formatted(1, "test", 5)
        );
        verify(commandDAO).giveSubscription(1, 5);
    }

    @Test
    public void GiveCommandTest_noUserTest() throws SQLException {
        when(repository.getUser(anyInt())).thenReturn(null);

        Command command = new GiveCommand(1, 5);

        assertEquals(
                commandExecutor.executeCommand(command),
                "No such user!"
        );
        verify(commandDAO, never()).giveSubscription(1, 5);
    }

    @Test
    public void GiveCommandTest_activeSubscriptionTest() throws SQLException {
        when(repository.getUser(anyInt())).thenReturn(new User(1, "test"));
        when(repository.getLastSubscription(anyInt())).thenReturn(new Subscription(1, now.minusDays(5), now.plusDays(5)));

        Command command = new GiveCommand(1, 5);

        assertEquals(
                commandExecutor.executeCommand(command),
                "Given user still has subscription"
        );
        verify(commandDAO, never()).giveSubscription(1, 5);
    }
}
