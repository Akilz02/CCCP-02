import adapter.ReportAdapter;
//import reports.BillReportTemplate;
//import reports.DailySalesReportTemplate;
//import reports.ReorderReportTemplate;
//import reports.StockReportTemplate;
import reports.Report;
import reports.ReportFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReportFactoryTest {

    @Test
    public void should_CreateDailySalesReport_given_DailySalesType() {
        Report report = ReportFactory.createReport("dailysales");
        assertNotNull(report);
        assertTrue(report instanceof ReportAdapter);
    }

    @Test
    public void should_CreateStockReport_given_StockType() {
        Report report = ReportFactory.createReport("stock");
        assertNotNull(report);
        assertTrue(report instanceof ReportAdapter);
    }

    @Test
    public void should_CreateBillReport_given_BillType() {
        Report report = ReportFactory.createReport("bill");
        assertNotNull(report);
        assertTrue(report instanceof ReportAdapter);
    }

    @Test
    public void should_CreateReorderReport_given_ReorderType() {
        Report report = ReportFactory.createReport("reorder");
        assertNotNull(report);
        assertTrue(report instanceof ReportAdapter);
    }

    @Test
    public void should_ThrowException_when_UnknownReportType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ReportFactory.createReport("unknown");
        });

        String expectedMessage = "Unknown report type";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}