import models.Bill;
import models.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import strategy.BillingStrategy;
import strategy.StandardBillingStrategy;
import services.BillingService;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BillingServiceTest {

    @Mock
    private BillingStrategy mockStrategy;
    private BillingService billingService;
    private Bill bill;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        billingService = new BillingService();
        billingService.setBillingStrategy(mockStrategy);
        Item item = new Item("I001", "Item1");
        item.setUnitPrice(100.0);
        item.setQuantity(2);
        bill = new Bill();
        bill.setItems(Collections.singletonList(item));
    }

    @Test
    public void should_ReturnCorrectTotal_when_CalculateTotal() {
        when(mockStrategy.calculateTotal(bill)).thenReturn(180.0);
        double total = billingService.handleBilling(bill);
        verify(mockStrategy, times(1)).calculateTotal(bill);
        assertEquals(180.0, total);
    }
}
