import dao.impl.ItemDAOImpl;
import models.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ItemDAOImplTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;
    private ItemDAOImpl itemDAO;
    private Item item;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        itemDAO = new ItemDAOImpl(mockConnection);
        item = new Item("I001", "Test Item");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    public void should_AddItemSuccessfully_when_AddItemCalled() throws SQLException {
        itemDAO.addItem(item);
        verify(mockPreparedStatement, times(1)).setString(1, item.getItemId());
        verify(mockPreparedStatement, times(1)).setString(2, item.getName());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test

    public void should_ReturnCorrectItem_when_GetItemById() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("item_id")).thenReturn("I001");
        when(mockResultSet.getString("name")).thenReturn("Test Item");
        Item retrievedItem = itemDAO.getItemById("I001");
        assertEquals(item.getItemId(), retrievedItem.getItemId());
        assertEquals(item.getName(), retrievedItem.getName());
    }
}
