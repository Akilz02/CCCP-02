import org.junit.jupiter.api.Test;
import singleton.DatabaseConnection;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class DatabaseConnectionTest {

    @Test
    public void should_ReturnSameInstance_when_GetInstanceCalledMultipleTimes() throws Exception {
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void should_ReturnValidConnection_when_ConnectionRequested() throws Exception {
        DatabaseConnection instance = DatabaseConnection.getInstance();
        Connection connection = instance.getConnection();
        assertNotNull(connection);
    }
}