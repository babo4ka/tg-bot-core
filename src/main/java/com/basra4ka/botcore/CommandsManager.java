package com.basra4ka.botcore;

import com.basra4ka.botcore.commands.CommandCoreException;
import com.basra4ka.botcore.commands.CommandsCore;
import com.basra4ka.botcore.commands.NotACommandCommand;
import com.basra4ka.botcore.commands.UnknownCommand;
import com.basra4ka.botcore.commands.interfaces.Command;
import com.basra4ka.botcore.commands.typing.CommandType;
import com.basra4ka.botcore.typing.MessageType;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandsManager {

    private static CommandsManager instance;

    private List<Long> owners = new ArrayList<>();

    public static CommandsManager getInstance() throws CommandCoreException {
        if(instance == null){
            throw new CommandCoreException("Менеджер комманд ещё не создан!");
        }
        return instance;
    }

    private final String textForStartCommand;
    private final String callStartCommand;

    public static CommandsManager setInstance(UnknownCommand unknownCommand, NotACommandCommand notACommandCommand,
                                              List<Long> owners,
                                              String textForStartCommand, String callStartCommand) throws CommandCoreException {
        if(owners.size() == 0){
            throw new CommandCoreException("Должен быть хотя бы один владелец бота");
        }

        if(instance == null){
            if(unknownCommand == null){
                unknownCommand = new UnknownCommand("Вернуться в начало",
                        (CommandsCore.getInstance().getPrefix() + "start"),
                        "Я Вас не понимаю :(",
                        MessageType.MESSAGE,
                        null);
            }
            if(notACommandCommand == null){
                notACommandCommand = new NotACommandCommand("Вернуться в начало",
                        (CommandsCore.getInstance().getPrefix() + "start"),
                        ("Неправильный префикс комманды, начните сообщение с " + CommandsCore.getInstance().getPrefix()),
                        MessageType.MESSAGE,
                        null);
            }

            instance = new CommandsManager(unknownCommand, notACommandCommand, owners, textForStartCommand, callStartCommand);
            return instance;
        }

        return instance;
    }

    private CommandsManager(UnknownCommand unknownCommand, NotACommandCommand notACommandCommand, List<Long> owners,
                            String textForStartCommand, String callStartCommand){
        this.commands.put(unknownCommand.getName(), unknownCommand);
        this.commands.put(notACommandCommand.getName(), notACommandCommand);

        this.owners.addAll(owners);
        if(textForStartCommand.equals(""))
            this.textForStartCommand = "Вернуться в начало";
        else this.textForStartCommand = textForStartCommand;

        if(callStartCommand.equals(""))
            this.callStartCommand = "/start";
        else this.callStartCommand = callStartCommand;
    }


    private Map<String, Command> commands = new HashMap<>();

    public List<String> getCommandNames(){
        List<String> names = new ArrayList<>();

        for(String c:commands.keySet()){
            names.add(commands.get(c).getName());
        }

        return names;
    }

    public void addCommand(Command command){
        commands.put(command.getName(), command);
    }

    public void addCommands(List<Command> commands){
        commands.forEach(c -> this.commands.put(c.getName(), c));
    }

    public Command getCommandByName(String name){
        return commands.get(name);
    }


    private final MessageCreator creator = new MessageCreator();
    public List<Message> executeCommand(Update update, String command, List<Argument> arguments) throws CommandCoreException {
        if(!command.startsWith(CommandsCore.getInstance().getPrefix())){
            return this.commands.get("not-a-command").execute(update, arguments);
        }

        Command commandFromMap = this.commands.get(command.substring(1));

        if(commandFromMap == null){
            return this.commands.get("unknown-command").execute(update, arguments);
        }else{
            long chatId = update.hasMessage()?
                    update.getMessage().getChatId():
                    update.getCallbackQuery().getMessage().getChatId();

            if(commandFromMap.getCommandType() == CommandType.STAFF){
                return  this.owners.contains(chatId)?
                        commandFromMap.execute(update, arguments):
                        notAccessed(chatId);
            }else{
                return commandFromMap.execute(update, arguments);
            }
        }
    }

    private List<Message> notAccessed(long chatId) throws CommandCoreException {
        List<List<HashMap<String, String>>> data = new ArrayList<>();
        List<HashMap<String, String>> btns = new ArrayList<>();

        btns.add(new HashMap<>(){{
            put(CommandsCore.getInstance().getTextForCallbackCall(), textForStartCommand);
            put(CommandsCore.getInstance().getCallbackCall(), callStartCommand);
        }});
        data.add(btns);

        MessageCreator.InlineKeyboardData keyboardData = new MessageCreator.InlineKeyboardData(data);

        return List.of(creator.createTextMessage(
                chatId,
                "У вас нет прав выполнять данную комманду",
                true,
                keyboardData,
                MessageCreator.KeyboardType.INLINE));

    }
}
