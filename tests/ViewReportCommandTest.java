import command.ViewReportCommand;
import facade.StoreFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class ViewReportCommandTest {

    @Mock
    private StoreFacade mockStoreFacade;
    private ViewReportCommand viewReportCommand;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewReportCommand = new ViewReportCommand(mockStoreFacade, "stock");
    }

    @Test
    public void should_GenerateStockReport_when_ExecuteCalled() {
        viewReportCommand.execute();
        verify(mockStoreFacade, times(1)).generateReport("stock");
    }

    @Test
    public void should_GenerateBillReport_when_ExecuteCalled() {
        viewReportCommand = new ViewReportCommand(mockStoreFacade, "bill");
        viewReportCommand.execute();
        verify(mockStoreFacade, times(1)).generateReport("bill");
    }

    @Test
    public void should_GenerateDailySalesReport_when_ExecuteCalled() {
        viewReportCommand = new ViewReportCommand(mockStoreFacade, "dailysales");
        viewReportCommand.execute();
        verify(mockStoreFacade, times(1)).generateReport("dailysales");
    }

    @Test
    public void should_GenerateReorderReport_when_ExecuteCalled() {
        viewReportCommand = new ViewReportCommand(mockStoreFacade, "reorder");
        viewReportCommand.execute();
        verify(mockStoreFacade, times(1)).generateReport("reorder");
    }
}