package com.foxandr.telegrambot.commands;

import com.foxandr.telegrambot.commands.base.HelpCommand;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class CommandContainer {
    private ApplicationContext applicationContext;
    private Set<Command> setOfCommands;
    private Map<String, Command> commandsByName;
    private List<Command> sortedListOfCommands;

    @Autowired
    public CommandContainer(Set<Command> setOfCommands, ApplicationContext applicationContext) {
        this.setOfCommands = setOfCommands;
        this.applicationContext = applicationContext;
        initCommandsByName(setOfCommands);
        initSortedListOfCommands(setOfCommands);
    }

    private void initCommandsByName(Set<Command> setOfCommands) {
        commandsByName = new HashMap<>();
        for (Command command : setOfCommands) {
            log.info("COMMAND [{}] added to command container!", command.getName());
            commandsByName.put(command.getName(), command);
            for (String alias : command.getAliases()) {
                commandsByName.put(alias, command);
            }
        }
    }

    public void initSortedListOfCommands(Set<Command> setOfCommands) {
        sortedListOfCommands = new ArrayList<>(setOfCommands);
        sortedListOfCommands.sort(((c1, c2) -> Integer.compare(c1.getHelpOrderPriority(), c2.getHelpOrderPriority())));
    }

    public Map<String, Command> getCommandsByName() {
        return commandsByName;
    }

    public List<Command> getSortedListOfCommands() {
        return sortedListOfCommands;
    }

    public Command retrieveCommand(String commandName) {
        return commandsByName.get(commandName);
    }

    @PostConstruct
    public void fillHelpCommand() {
        HelpCommand helpCommand = applicationContext.getBean(HelpCommand.class);
        helpCommand.initHelpCommand(this);
    }
}
