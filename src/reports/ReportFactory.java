package reports;

import adapter.ReportAdapter;
import template.*;

import java.sql.Date;

public class ReportFactory {

    public static Report createReport(String type) {

        switch(type.toLowerCase()) {
            case "dailysales":
//                return new DailySalesReport((Date)params[0]);
                return new ReportAdapter(new DailySalesReportTemplate());
            case "stock":
//                return new StockReport();
                return new ReportAdapter(new StockReportTemplate());
            case "bill":
                return new ReportAdapter(new BillReportTemplate());
            case "reorder":
                return new ReportAdapter(new ReorderReportTemplate());
// Add other report types as needed
            default:
                throw new IllegalArgumentException("Unknown report type");
        }

    }

}