package com.basra4ka.botcore;


import com.basra4ka.botcore.commands.CommandCoreException;
import com.basra4ka.botcore.commands.CommandsCore;
import com.basra4ka.botcore.typing.MessageType;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MessageCreator {
    public MessageCreator(){}


    public Message createTextMessage(
            long chatId,
            String text,
            boolean markable,
            KeyboardData data,
            KeyboardType type
    ) throws CommandCoreException {
        SendMessage sendMessage = new SendMessage();

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setParseMode("HTML");

        sendMessage.setReplyMarkup(setKeyBoard(data, type));

        Message message = new Message(MessageType.MESSAGE, markable);
        message.setSendMessage(sendMessage);

        return message;
    }

    public Message createMessageWithPhoto(
            long chatId,
            String text,
            InputFile photo,
            boolean markable,
            KeyboardData data,
            KeyboardType type
    ) throws CommandCoreException {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(photo);
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(text);
        sendPhoto.setParseMode("HTML");

        sendPhoto.setReplyMarkup(setKeyBoard(data, type));

        Message message = new Message(MessageType.PHOTO, markable);
        message.setSendPhoto(sendPhoto);

        return message;
    }

    public Message createMessageWithDocument(
            long chatId,
            String text,
            InputFile document,
            boolean markable,
            KeyboardData data,
            KeyboardType type
    ) throws CommandCoreException {
        SendDocument sendDocument = new SendDocument();

        sendDocument.setDocument(document);
        sendDocument.setChatId(chatId);
        sendDocument.setCaption(text);
        sendDocument.setParseMode("HTML");

        sendDocument.setReplyMarkup(setKeyBoard(data, type));


        Message message = new Message(MessageType.DOCUMENT, markable);
        message.setSendDocument(sendDocument);

        return message;
    }

    private ReplyKeyboard setKeyBoard(KeyboardData data, KeyboardType type) throws CommandCoreException {
        switch (type) {
            case INLINE -> {
                InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
                InlineKeyboardData localData = (InlineKeyboardData) data;

                List<List<InlineKeyboardButton>> rows = new ArrayList<>();
                List<InlineKeyboardButton> btns = new ArrayList<>();

                if (localData != null) {
                    for (List<HashMap<String, String>> buttons : localData.buttonsData) {

                        for (HashMap<String, String> button : buttons) {
                            btns.add(new InlineKeyboardButton().builder()
                                    .text(button.get(CommandsCore.getInstance().getTextForCallbackCall()))
                                    .callbackData(button.get(CommandsCore.getInstance().getTextForCallbackCall())).build());
                        }
                        rows.add(btns);
                        btns = new ArrayList<>();
                    }
                    keyboard.setKeyboard(rows);
                    return keyboard;
                }
            }

            case REPLY -> {
                ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
                List<KeyboardRow> rows = new ArrayList<>();
                KeyboardRow row;
                ReplyKeyboardData localData = (ReplyKeyboardData) data;

                if (localData != null) {
                    keyboard.setSelective(localData.selective);
                    keyboard.setResizeKeyboard(localData.resize);
                    keyboard.setOneTimeKeyboard(localData.oneTime);

                    for (List<String> buttonsRow : localData.buttons) {
                        row = new KeyboardRow();

                        for (String cmd : buttonsRow) {
                            row.add(new KeyboardButton(cmd));
                        }
                        rows.add(row);
                    }

                    keyboard.setKeyboard(rows);
                }
                return keyboard;
            }
        }
        return null;
    }


    public enum KeyboardType{
        INLINE,
        REPLY
    }

    public static class InlineKeyboardData implements KeyboardData{
        private List<List<HashMap<String, String>>> buttonsData;

        public InlineKeyboardData(List<List<HashMap<String, String>>> buttonsData){
            this.buttonsData = buttonsData;
        }

        public List<List<HashMap<String, String>>> getButtonsData() {
            return buttonsData;
        }
    }

    public static class ReplyKeyboardData implements KeyboardData{
        private boolean selective;
        private boolean resize;
        private boolean oneTime;
        List<List<String>> buttons;

        public ReplyKeyboardData(boolean selective, boolean resize, boolean oneTime, List<List<String>> buttons) {
            this.selective = selective;
            this.resize = resize;
            this.oneTime = oneTime;
            this.buttons = buttons;
        }

        public boolean isSelective() {
            return selective;
        }

        public boolean isResize() {
            return resize;
        }

        public boolean isOneTime() {
            return oneTime;
        }

        public List<List<String>> getButtons() {
            return buttons;
        }

    }


    private interface KeyboardData{
    }
}
