package command;

import facade.StoreFacade;

public class ViewReportCommand implements Command {

    private StoreFacade storeFacade;

    private String reportType;

    private Object[] params;

    public ViewReportCommand(StoreFacade storeFacade, String reportType, Object... params) {

        this.storeFacade = storeFacade;

        this.reportType = reportType;

        this.params = params;

    }

    @Override

    public void execute() {

        storeFacade.generateReport(reportType);

    }

}
