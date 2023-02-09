package com.basra4ka.botcore.commands;

import com.basra4ka.botcore.Argument;
import com.basra4ka.botcore.Message;
import com.basra4ka.botcore.MessageCreator;
import com.basra4ka.botcore.commands.interfaces.Command;
import com.basra4ka.botcore.commands.typing.CommandType;
import com.basra4ka.botcore.typing.MessageType;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotACommandCommand implements Command {

    MessageCreator creator = new MessageCreator();

    private final String textToOut;
    private final String callbackCommand;
    private final String notACommandText;
    private final MessageType typeOfMessage;

    private final File fileToSend;

    public NotACommandCommand(String textToOut, String callbackCommand, String notACommandText,
                              MessageType typeOfMessage, File fileToSend) throws CommandCoreException {
        if(!callbackCommand.startsWith(CommandsCore.getInstance().getPrefix())){
            throw new CommandCoreException("Неверный префикс");
        }

        this.textToOut = textToOut;
        this.callbackCommand = callbackCommand;
        this.notACommandText = notACommandText;
        this.typeOfMessage = typeOfMessage;
        this.fileToSend = fileToSend;
    }

    @Override
    public String getName() {
        return "not-a-command";
    }


    @Override
    public CommandType getCommandType() {
        return CommandType.EVERYONE;
    }

    @Override
    public List<Argument> getArgs() {
        return null;
    }

    @Override
    public Argument getArgumentByName(String name) {
        return null;
    }

    @Override
    public void addArgument(Argument a) {

    }

    @Override
    public void addArguments(List<Argument> args) {

    }

    @Override
    public List<Message> execute(Update update, List<Argument> arguments) throws CommandCoreException {
        List<Message> outMessages = new ArrayList<>();

        long chatId = update.hasMessage()?update.getMessage().getChatId()
                :update.getCallbackQuery().getMessage().getChatId();

        List<List<HashMap<String, String>>> data = new ArrayList<>();
        List<HashMap<String, String>> btns = new ArrayList<>();

        btns.add(new HashMap<>(){{
            put(CommandsCore.getInstance().getTextForCallbackCall(), textToOut);
            put(CommandsCore.getInstance().getCallbackCall(), callbackCommand);
        }});
        data.add(btns);

        MessageCreator.InlineKeyboardData keyboardData = new MessageCreator.InlineKeyboardData(data);

        switch (typeOfMessage){
            case MESSAGE -> outMessages.add(creator.createTextMessage(
                    chatId,
                    notACommandText,
                    true,
                    keyboardData,
                    MessageCreator.KeyboardType.INLINE
            ));

            case PHOTO -> outMessages.add(creator.createMessageWithPhoto(
                    chatId,
                    notACommandText,
                    new InputFile(fileToSend),
                    true,
                    keyboardData,
                    MessageCreator.KeyboardType.INLINE
            ));

            case DOCUMENT -> outMessages.add(creator.createMessageWithDocument(
                    chatId,
                    notACommandText,
                    new InputFile(fileToSend),
                    true,
                    keyboardData,
                    MessageCreator.KeyboardType.INLINE
            ));

        }

        return outMessages;
    }

    @Override
    public List<Message> process(Update update, List<Argument> arguments) throws CommandCoreException {
        return null;
    }

    @Override
    public void resetCommand() {

    }
}
