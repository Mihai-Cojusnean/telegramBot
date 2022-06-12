package org.example.telegramservice.commands;

import org.example.telegramservice.service.BtnManager;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class OracleTestCmd implements CommandGenerator<SendMessage> {

    @Override
    public SendMessage generate(Update update) {
        BtnManager btnManager = new BtnManager();
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText("Choose topic");
        message.setReplyMarkup(btnManager.setInlineKeyboardMarkup(new String[][]{{"Java Data Types", "Program Flow"}}));
        return message;
    }

    @Override
    public String[] getInputCommand() {
        return new String[]{"Oracle prob"};
    }
}
