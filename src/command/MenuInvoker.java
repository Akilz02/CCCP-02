package command;

import java.util.HashMap;

import java.util.Map;

public class MenuInvoker {

    private Map<Integer, Command> commandMap = new HashMap<>();

    public void setCommand(int option, Command command) {

        commandMap.put(option, command);

    }

    public void executeCommand(int option) {

        Command command = commandMap.get(option);

        if(command != null) {

            command.execute();

        }

    }

}
