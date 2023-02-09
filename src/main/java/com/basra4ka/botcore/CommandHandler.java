package com.basra4ka.botcore;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


public abstract class CommandHandler {

    private int stepNum;
    private CommandHandler next;

    public CommandHandler(int stepNum){
        this.stepNum = stepNum;
    }

    public void setNextHandler(CommandHandler handler){
        this.next = handler;
    }

    public void notifyHandler(Update update, List<Argument> arguments, int step){
        if(step == this.stepNum){
            process(update, arguments);
        }
        else if(step > this.stepNum){
            if(next != null){
                next.notifyHandler(update, arguments, step);
            }
        }
    }

    public abstract List<Message> process(Update update, List<Argument> arguments);
}
