package org.example.telegramservice.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UnSupportedCommand implements CommandGenerator<SendMessage> {
    @Override
    public SendMessage generate(Update update) {
        return new SendMessage(update.getMessage().getChatId().toString(),
                "Unfortunately, I'm not Alexa or Siri to reply at everything :(," +
                        " but i have some command like: /start or Oracle prob");
    }

    @Override
    public String[] getInputCommand() {
        return new String[]{"un_supported_command"};
    }
}
