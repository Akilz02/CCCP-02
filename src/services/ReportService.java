package services;

import reports.Report;
import reports.ReportFactory;
import java.sql.Date;

public class ReportService {

    public void generate(String reportType) {
        Report report = ReportFactory.createReport(reportType);
        report.generateReport();

    }

}
