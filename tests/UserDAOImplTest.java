import dao.impl.UserDAOImpl;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDAOImplTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;
    private UserDAOImpl userDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        userDAO = new UserDAOImpl(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

//    @Test
//    public void should_AddUserSuccessfully_when_AddUserCalled() throws SQLException {
//        User user = new User("U001", "testUser", "password123");
//        userDAO.addUser(user);
//        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
//        verify(mockConnection).prepareStatement(sqlCaptor.capture());
//        assertEquals("INSERT INTO user (user_id, username, password) VALUES (?, ?, ?)", sqlCaptor.getValue());
//
//        verify(mockPreparedStatement).setString(1, user.getUserId());
//        verify(mockPreparedStatement).setString(2, user.getUsername());
//        verify(mockPreparedStatement).setString(3, user.getPassword());
//        verify(mockPreparedStatement).executeUpdate();
//    }

    @Test
    public void should_GetUserByUsernameSuccessfully_when_UserExists() throws SQLException {
        String username = "testUser";
        String password = "password123";
        User expectedUser = new User("U001", username, password);

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("user_id")).thenReturn(expectedUser.getUserId());
        when(mockResultSet.getString("username")).thenReturn(expectedUser.getUsername());
        when(mockResultSet.getString("password")).thenReturn(expectedUser.getPassword());

        User actualUser = userDAO.getUserByUsername(username);

        assertNotNull(actualUser);
        assertEquals(expectedUser.getUserId(), actualUser.getUserId());
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());

        verify(mockPreparedStatement).setString(1, username);
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void should_ReturnNull_when_GetUserByUsernameCalledWithNonExistentUser() throws SQLException {
        String username = "nonExistentUser";
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        User actualUser = userDAO.getUserByUsername(username);
        assertNull(actualUser);
        verify(mockPreparedStatement).setString(1, username);
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void should_ValidateUserSuccessfully_when_CredentialsAreCorrect() throws SQLException {
        String username = "testUser";
        String password = "password123";
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        boolean isValid = userDAO.validateUser(username, password);
        assertTrue(isValid);

        verify(mockPreparedStatement).setString(1, username);
        verify(mockPreparedStatement).setString(2, password);
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void should_FailValidation_when_CredentialsAreIncorrect() throws SQLException {
        String username = "testUser";
        String password = "wrongPassword";

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(0);

        boolean isValid = userDAO.validateUser(username, password);
        assertFalse(isValid);

        verify(mockPreparedStatement).setString(1, username);
        verify(mockPreparedStatement).setString(2, password);
        verify(mockPreparedStatement).executeQuery();
    }
}