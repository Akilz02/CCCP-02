import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reports.Report;
import template.AbstractReport;
import adapter.ReportAdapter;
import static org.mockito.Mockito.*;

public class ReportAdapterTest {

    @Mock
    private AbstractReport mockAbstractReport;
    private Report reportAdapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reportAdapter = new ReportAdapter(mockAbstractReport);
    }

    @Test
    public void should_DelegateReportGeneration_when_GenerateReportCalled() {
        reportAdapter.generateReport();
        verify(mockAbstractReport, times(1)).generateReport();
    }
}