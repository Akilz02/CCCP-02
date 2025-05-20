package reports;

import java.sql.Date;

public class DailySalesReport implements Report {
    private Date date;

    public DailySalesReport(Date date) {
        this.date = date;

    }

    @Override

    public void generateReport() {

// Implementation for generating daily sales report

        System.out.println("Generating Daily Sales Report for date: " + date);

// Actual report generation logic goes here

    }

}
