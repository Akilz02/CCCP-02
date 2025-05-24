import facade.StoreFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import singleton.DatabaseConnection;

import models.Bill;
import models.Item;
import strategy.BillingStrategy;
import strategy.StandardBillingStrategy;
import strategy.DiscountBillingStrategy;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class StoreFacadeTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private DatabaseConnection mockDbConnection;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        // Set up common mocks
        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    public void testGetBillId() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            // Mock the result of COUNT query
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(5);

            int billId = StoreFacade.getBillId();

            // Expected bill ID should be count + 1
            assertEquals(6, billId);
            verify(mockPreparedStatement).executeQuery();
        }
    }

    @Test
    public void testSaveToBillTable() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            Date billDate = new Date(System.currentTimeMillis());
            StoreFacade.saveToBillTable(123, 99.99, 100.00, 0.01, billDate);

            verify(mockPreparedStatement).setInt(1, 123);
            verify(mockPreparedStatement).setDouble(2, 99.99);
            verify(mockPreparedStatement).setDouble(3, 100.00);
            verify(mockPreparedStatement).setDouble(4, 0.01);
            verify(mockPreparedStatement).setDate(5, billDate);
            verify(mockPreparedStatement).executeUpdate();
        }
    }

    @Test
    public void testSaveToBillItemTable() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            // Mock result set for temp_bill items
            when(mockResultSet.next()).thenReturn(true, true, false); // Two items in the bill
            when(mockResultSet.getString("item_id")).thenReturn("item1", "item2");
            when(mockResultSet.getString("batch_id")).thenReturn("batch1", "batch2");
            when(mockResultSet.getInt("quantity")).thenReturn(2, 3);
            when(mockResultSet.getDouble("unit_price")).thenReturn(10.99, 5.99);
            when(mockResultSet.getDouble("total_price")).thenReturn(21.98, 17.97);

            // Mock the prepared statement for insertion
            PreparedStatement insertStatement = mock(PreparedStatement.class);
            when(mockConnection.prepareStatement(contains("INSERT INTO bill_item"))).thenReturn(insertStatement);
            when(insertStatement.executeUpdate()).thenReturn(1);

            StoreFacade.saveToBillItemTable(123);

            verify(mockPreparedStatement).executeQuery();
            verify(insertStatement, times(2)).executeUpdate();
        }
    }

    @Test
    public void testRemoveItemsFromShelf() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            // Mock result set for temp_bill items
            when(mockResultSet.next()).thenReturn(true, true, false); // Two items in the bill
            when(mockResultSet.getString("item_id")).thenReturn("item1", "item2");
            when(mockResultSet.getString("batch_id")).thenReturn("batch1", "batch2");
            when(mockResultSet.getInt("quantity")).thenReturn(2, 3);

            // Mock the prepared statement for update
            PreparedStatement updateStatement = mock(PreparedStatement.class);
            when(mockConnection.prepareStatement(contains("UPDATE shelf_item"))).thenReturn(updateStatement);
            when(updateStatement.executeUpdate()).thenReturn(1);

            StoreFacade.removeItemsFromShelf();

            verify(mockPreparedStatement).executeQuery();
            verify(updateStatement, times(2)).executeUpdate();
        }
    }

    @Test
    public void testClearTempBill() throws SQLException {
        try (MockedStatic<DatabaseConnection> dbConnectionMock = mockStatic(DatabaseConnection.class)) {
            dbConnectionMock.when(DatabaseConnection::getInstance).thenReturn(mockDbConnection);

            when(mockPreparedStatement.executeUpdate()).thenReturn(5); // 5 rows deleted

            StoreFacade.clearTempBill();

            verify(mockPreparedStatement).executeUpdate();
        }
    }

    @Test
    public void testProcessBilling_StandardStrategy() throws Exception {
        // Setup bill with items
        Bill bill = new Bill();
        List<Item> items = new ArrayList<>();
        items.add(new Item("item1", "batch1", 2, 10.99));
        items.add(new Item("item2", "batch2", 1, 5.99));
        bill.setItems(items);

        // Create strategy and mock user input
        StandardBillingStrategy strategy = mock(StandardBillingStrategy.class);
        when(strategy.calculateTotal(any(Bill.class))).thenReturn(27.97); // 2*10.99 + 5.99
        when(strategy.getCashTendered()).thenReturn(30.00);
        when(strategy.getChange()).thenReturn(2.03);

        double total = StoreFacade.processBilling(bill, strategy);

        assertEquals(27.97, total, 0.01);
        verify(strategy).calculateTotal(bill);
    }

    @Test
    public void testProcessBilling_DiscountStrategy() throws Exception {
        // Setup bill with items
        Bill bill = new Bill();
        List<Item> items = new ArrayList<>();
        items.add(new Item("item1", "batch1", 2, 10.99));
        items.add(new Item("item2", "batch2", 1, 5.99));
        bill.setItems(items);

        // Create strategy and mock user input
        DiscountBillingStrategy strategy = mock(DiscountBillingStrategy.class);
        when(strategy.calculateTotal(any(Bill.class))).thenReturn(25.17); // 10% discount on 27.97
        when(strategy.getCashTendered()).thenReturn(30.00);
        when(strategy.getChange()).thenReturn(4.83);
        when(strategy.getDiscountAmount()).thenReturn(2.80);

        double total = StoreFacade.processBilling(bill, strategy);

        assertEquals(25.17, total, 0.01);
        verify(strategy).calculateTotal(bill);
    }
}