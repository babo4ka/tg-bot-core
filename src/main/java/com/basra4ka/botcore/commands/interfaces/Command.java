package com.basra4ka.botcore.commands.interfaces;


import com.basra4ka.botcore.Argument;
import com.basra4ka.botcore.Message;
import com.basra4ka.botcore.commands.CommandCoreException;
import com.basra4ka.botcore.commands.typing.CommandType;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface Command {
    String getName();
    CommandType getCommandType();
    List<Argument> getArgs();
    Argument getArgumentByName(String name);
    void addArgument(Argument a);
    void addArguments(List<Argument> args);

    List<Message> execute(Update update, List<Argument> arguments) throws CommandCoreException;
    List<Message> process(Update update, List<Argument> arguments) throws CommandCoreException;

    default int getStep(){return 1;}
    void resetCommand();
}
