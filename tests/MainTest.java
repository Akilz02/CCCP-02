import main.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import singleton.DatabaseConnection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MainTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private DatabaseConnection mockDbConnection;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() throws Exception {
        // Set up database mocks
        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Redirect System.out for testing console output
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testGetUserID() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getString("user_id")).thenReturn("user123");

            String userId = Main.getUserID("testuser");

            assertEquals("user123", userId);
            verify(mockPreparedStatement).setString(1, "testuser");
            verify(mockPreparedStatement).executeQuery();
        }
    }

    @Test
    void testGetUserID_UserNotFound() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            when(mockResultSet.next()).thenReturn(false);

            String userId = Main.getUserID("nonexistentuser");

            assertNull(userId);
            verify(mockPreparedStatement).setString(1, "nonexistentuser");
            verify(mockPreparedStatement).executeQuery();
        }
    }

    @Test
    void testSaveToCart() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            Main.saveToCart("item123", 2, "user123");

            verify(mockPreparedStatement).setString(1, "user123");
            verify(mockPreparedStatement).setString(2, "item123");
            verify(mockPreparedStatement).setInt(3, 2);
            verify(mockPreparedStatement).executeUpdate();
        }
    }

    @Test
    void testGetOnlineBillNo() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(5);

            int billNo = Main.getOnlineBillNo();

            assertEquals(6, billNo); // Should be last bill number + 1
            verify(mockPreparedStatement).executeQuery();
        }
    }

    @Test
    void testGetOnlineBillNo_NoBillsExist() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            when(mockResultSet.next()).thenReturn(false);

            int billNo = Main.getOnlineBillNo();

            assertEquals(1, billNo); // Should be 1 if no bills exist
            verify(mockPreparedStatement).executeQuery();
        }
    }

    @Test
    void testSaveOnlineBill() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            Date billDate = new Date(System.currentTimeMillis());
            Main.saveOnlineBill(123, "user123", 99.99, billDate);

            verify(mockPreparedStatement).setInt(1, 123);
            verify(mockPreparedStatement).setString(2, "user123");
            verify(mockPreparedStatement).setDouble(3, 99.99);
            verify(mockPreparedStatement).setDate(4, billDate);
            verify(mockPreparedStatement).executeUpdate();
        }
    }

    @Test
    void testCheckItemAvailabilityInStock() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            when(mockResultSet.next()).thenReturn(true);

            boolean available = Main.checkItemAvailabilityInStock("item123");

            assertTrue(available);
            verify(mockPreparedStatement).setString(1, "item123");
            verify(mockPreparedStatement).executeQuery();
        }
    }

    @Test
    void testCheckItemAvailabilityInStock_NotAvailable() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            when(mockResultSet.next()).thenReturn(false);

            boolean available = Main.checkItemAvailabilityInStock("nonexistentitem");

            assertFalse(available);
            verify(mockPreparedStatement).setString(1, "nonexistentitem");
            verify(mockPreparedStatement).executeQuery();
        }
    }


    //*********

    @Test
    void testSaveOnlineBillItems() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            // Mock the result set for cart items
            ResultSet cartItemsResultSet = mock(ResultSet.class);
            when(mockPreparedStatement.executeQuery()).thenReturn(cartItemsResultSet);
            when(cartItemsResultSet.next()).thenReturn(true, true, false); // Two items in cart
            when(cartItemsResultSet.getString("item_id")).thenReturn("item1", "item2");
            when(cartItemsResultSet.getInt("quantity")).thenReturn(2, 1);
            when(cartItemsResultSet.getDouble("unit_price")).thenReturn(10.99, 5.99);

            // Mock the prepared statement for insertion
            PreparedStatement insertStatement = mock(PreparedStatement.class);
            when(mockConnection.prepareStatement(contains("INSERT INTO online_bill_item"))).thenReturn(insertStatement);
            when(insertStatement.executeUpdate()).thenReturn(1);

            Main.saveOnlineBillItems(123, "user123");

            verify(mockPreparedStatement).setString(1, "user123");
            verify(insertStatement, times(2)).executeUpdate();
        }
    }

    @Test
    void testCheckBatchAvailabilityInStock() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(1);

            boolean result = Main.checkBatchAvailabilityInStock("item123", "batch123");

            assertTrue(result);
            verify(mockPreparedStatement).setString(1, "item123");
            verify(mockPreparedStatement).setString(2, "batch123");
            verify(mockPreparedStatement).executeQuery();
        }
    }

    @Test
    void testCheckBatchAvailabilityInStock_NotAvailable() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(0);

            boolean result = Main.checkBatchAvailabilityInStock("item123", "nonexistentbatch");

            assertFalse(result);
            verify(mockPreparedStatement).executeQuery();
        }
    }

    @Test
    void testSaveBatchToStock() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            Date purchaseDate = new Date(System.currentTimeMillis());
            Date expiryDate = new Date(System.currentTimeMillis() + 86400000); // tomorrow

            Main.saveBatchToStock("item123", "batch123", 10, 15.99, purchaseDate, expiryDate);

            verify(mockPreparedStatement).setString(1, "item123");
            verify(mockPreparedStatement).setString(2, "batch123");
            verify(mockPreparedStatement).setInt(3, 10);
            verify(mockPreparedStatement).setDouble(4, 15.99);
            verify(mockPreparedStatement).setDate(5, purchaseDate);
            verify(mockPreparedStatement).setDate(6, expiryDate);
            verify(mockPreparedStatement).executeUpdate();
        }
    }

    @Test
    void testCheckShelfAvailability() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(1);

            boolean result = Main.checkShelfAvailability("shelf123");

            assertTrue(result);
            verify(mockPreparedStatement).setString(1, "shelf123");
            verify(mockPreparedStatement).executeQuery();
        }
    }

    @Test
    void testCheckLogin() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(1);

            boolean result = Main.checkLogin("testuser", "password");

            assertTrue(result);
            verify(mockPreparedStatement).setString(1, "testuser");
            verify(mockPreparedStatement).setString(2, "password");
            verify(mockPreparedStatement).executeQuery();
        }
    }

    @Test
    void testCheckLogin_InvalidCredentials() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(0);

            boolean result = Main.checkLogin("testuser", "wrongpassword");

            assertFalse(result);
            verify(mockPreparedStatement).executeQuery();
        }
    }

    @Test
    void testClearCart() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            // Mock getUserID
            try (MockedStatic<Main> mainMock = mockStatic(Main.class)) {
                mainMock.when(() -> Main.getUserID("testuser")).thenReturn("user123");

                // Call the actual clearCart method
                mainMock.when(() -> Main.clearCart("testuser")).thenCallRealMethod();

                when(mockPreparedStatement.executeUpdate()).thenReturn(1);

                Main.clearCart("testuser");

                verify(mockPreparedStatement).setString(1, "user123");
                verify(mockPreparedStatement).executeUpdate();
            }
        }
    }

    @Test
    void testSaveShelf() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            Main.saveShelf("shelf123", 50);

            verify(mockPreparedStatement).setString(1, "shelf123");
            verify(mockPreparedStatement).setInt(2, 50);
            verify(mockPreparedStatement).executeUpdate();
        }
    }

    @Test
    void testIsCapacityLimitExceeded() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            // Create mocks for the two different result sets
            ResultSet shelfResultSet = mock(ResultSet.class);
            ResultSet capacityResultSet = mock(ResultSet.class);

            // First query for shelf capacity
            PreparedStatement shelfStatement = mock(PreparedStatement.class);
            when(mockConnection.prepareStatement(contains("SELECT capacity FROM shelf"))).thenReturn(shelfStatement);
            when(shelfStatement.executeQuery()).thenReturn(shelfResultSet);
            when(shelfResultSet.next()).thenReturn(true);
            when(shelfResultSet.getInt("capacity")).thenReturn(100);

            // Second query for current total capacity
            PreparedStatement capacityStatement = mock(PreparedStatement.class);
            when(mockConnection.prepareStatement(contains("SELECT SUM(capacity)"))).thenReturn(capacityStatement);
            when(capacityStatement.executeQuery()).thenReturn(capacityResultSet);
            when(capacityResultSet.next()).thenReturn(true);
            when(capacityResultSet.getInt("total_capacity")).thenReturn(80);

            boolean result = Main.isCapacityLimitExceeded("shelf123", 30);

            assertTrue(result); // 80 + 30 > 100

            verify(shelfStatement).setString(1, "shelf123");
            verify(capacityStatement).setString(1, "shelf123");
        }
    }
}