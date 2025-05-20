import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.StockService;
import static org.mockito.Mockito.*;

public class StockServiceTest {

    @Mock
    private StockService mockStockService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_UpdateStockSuccessfully_when_UpdateStockCalled() {
        doNothing().when(mockStockService).updateStock();
        mockStockService.updateStock();
        verify(mockStockService, times(1)).updateStock();
    }
}