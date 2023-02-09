package com.basra4ka.botcore;

import com.basra4ka.botcore.typing.MessageType;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

public class Message {

    private SendMessage sendMessage;
    private SendPhoto sendPhoto;
    private SendDocument sendDocument;

    private MessageType type;

    private boolean markable;

    public boolean isMarkable() {
        return markable;
    }

    public void setMarkable(boolean markable) {
        this.markable = markable;
    }


    public Message(MessageType type, boolean markable){
        this.type = type;
        this.markable = markable;
        switch (type){
            case MESSAGE:
                this.sendMessage = new SendMessage();
                break;

            case PHOTO:
                this.sendPhoto = new SendPhoto();
                break;

            case DOCUMENT:
                this.sendDocument = new SendDocument();
                break;
        }
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public SendMessage getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
    }

    public SendPhoto getSendPhoto() {
        return sendPhoto;
    }

    public void setSendPhoto(SendPhoto sendPhoto) {
        this.sendPhoto = sendPhoto;
    }

    public SendDocument getSendDocument() {
        return sendDocument;
    }

    public void setSendDocument(SendDocument sendDocument) {
        this.sendDocument = sendDocument;
    }

}
