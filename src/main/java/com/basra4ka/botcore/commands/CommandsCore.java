package com.basra4ka.botcore.commands;

public final class CommandsCore {

    private final String prefix;

    private final String textForCallbackCall;
    private final String callbackCall;

    public String getPrefix() throws CommandCoreException {
        if(this.prefix != null)return this.prefix;

        throw new CommandCoreException("Префикс ещё не задан!");
    }

    public String getTextForCallbackCall() throws CommandCoreException {
        if(this.textForCallbackCall!=null)return this.textForCallbackCall;

        throw new CommandCoreException("Обозначение текста не задано!");

    }

    public String getCallbackCall() throws CommandCoreException {
        if(this.callbackCall != null)return  this.callbackCall;

        throw new CommandCoreException("Обозначение комманды колбека не задано!");
    }

    private static CommandsCore instance;

    private CommandsCore(String prefix, String textForCallbackCall, String callbackCall){
        this.prefix = prefix;
        this.textForCallbackCall = textForCallbackCall;
        this.callbackCall = callbackCall;
    }

    public static CommandsCore setInstance(String prefix, String textForCallbackCall, String callbackCall){
        if(instance == null){
            instance = new CommandsCore(prefix, textForCallbackCall, callbackCall);
            return instance;
        }

        return instance;
    }

    public static CommandsCore getInstance() throws CommandCoreException {
        if(instance != null)return instance;

        throw new CommandCoreException("Префикс ещё не задан!");
    }


}
