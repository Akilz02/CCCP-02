package adapter;

import reports.Report;

import template.AbstractReport;

public class ReportAdapter implements Report {

    private AbstractReport abstractReport;

    public ReportAdapter(AbstractReport abstractReport) {

        this.abstractReport = abstractReport;

    }

    @Override

    public void generateReport() {

        abstractReport.generateReport();

    }

}
