package org.example.telegramservice.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BtnManager {

    public InlineKeyboardMarkup setInlineKeyboardMarkup(String[][] buttons) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (String[] button : buttons) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (String s : button) {
                InlineKeyboardButton inlineBtn = new InlineKeyboardButton();
                inlineBtn.setText(s);
                inlineBtn.setCallbackData(s);
                row.add(inlineBtn);
            }
            keyboard.add(row);
        }
        markupInline.setKeyboard(keyboard);

        return markupInline;
    }

    public ReplyKeyboardMarkup setStickyBtn(String[] btnName) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        List<KeyboardRow> keyboard = new ArrayList<>();

        Arrays.stream(btnName).forEach(btn -> keyboardFirstRow.add(new KeyboardButton(btn)));
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}