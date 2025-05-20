package command;

import facade.StoreFacade;

public class AddItemCommand implements Command {

    private StoreFacade storeFacade;

    public AddItemCommand(StoreFacade storeFacade) {

        this.storeFacade = storeFacade;

    }

    @Override

    public void execute() {

        storeFacade.addItemToStock();

    }

}
