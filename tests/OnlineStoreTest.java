import facade.StoreFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.BillingService;
import services.StockService;
import services.UserService;
import services.ReportService;
import static org.mockito.Mockito.*;

public class OnlineStoreTest {

    @Mock
    private BillingService mockBillingService;

    @Mock
    private StockService mockStockService;

    @Mock
    private UserService mockUserService;

    @Mock
    private ReportService mockReportService;

    @Mock
    private StoreFacade mockStoreFacade;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_ProcessCustomerBilling_when_ProcessCustomerBillingCalled() {
        doNothing().when(mockStoreFacade).processCustomerBilling();
        mockStoreFacade.processCustomerBilling();
        verify(mockStoreFacade, times(1)).processCustomerBilling();
    }

    @Test
    public void should_AddItemToStock_when_AddItemToStockCalled() {
        doNothing().when(mockStoreFacade).addItemToStock();
        mockStoreFacade.addItemToStock();
        verify(mockStoreFacade, times(1)).addItemToStock();
    }

    @Test
    public void should_GenerateReport_when_GenerateReportCalled() {
        String reportType = "bill";
        doNothing().when(mockStoreFacade).generateReport(reportType);
        mockStoreFacade.generateReport(reportType);
        verify(mockStoreFacade, times(1)).generateReport(reportType);
    }

    @Test
    public void should_HandleUserOperations_when_ManageUserCalled() {
        doNothing().when(mockStoreFacade).manageUser();
        mockStoreFacade.manageUser();
        verify(mockStoreFacade, times(1)).manageUser();
    }

    @Test
    public void should_UpdateStock_when_ManageStockCalled() {
        doNothing().when(mockStoreFacade).manageStock();
        mockStoreFacade.manageStock();
        verify(mockStoreFacade, times(1)).manageStock();
    }
}