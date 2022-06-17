package org.example.telegramservice.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import org.example.telegramservice.dbservice.entity.Test;
import org.example.telegramservice.dbservice.repository.TestRepo;
import org.example.telegramservice.service.BtnManager;
import org.example.telegramservice.service.ChConfig.ChConfig;
import org.example.telegramservice.service.ChConfig.Question;
import org.example.telegramservice.service.ChConfig.TestsConfig;
import org.example.telegramservice.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.List;

@Component
public class OracleTestCmd implements CommandGenerator<SendMessage> {

    List<String> commands = List.of("Data Types", "Program Flow", "Object‐Oriented Approach", "Exception Handling",
            "Arrays and Collections", "Streams and Lambda", "Platform Module System",
            "Concurrency", "I/O API", "Secure Coding", "Database with JDBC", "Localization",
            "Annotations", "Practice Exam 1", "Practice Exam 2", "Practice Exam 3", "Oracle prob");
    List<ChConfig> chapters;

    private final TestRepo testRepo;
    private final UserService userService;

    public OracleTestCmd(TestRepo testRepo, UserService userService) {
        this.testRepo = testRepo;
        this.userService = userService;
    }

    @Override
    @SneakyThrows
    public SendMessage generate(Update update) {
        File file = new File("src/main/resources/java_test.yaml");
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        TestsConfig testsConfig = objectMapper.readValue(file, TestsConfig.class);
        chapters = testsConfig.getChapters();
        BtnManager btnManager = new BtnManager();
        SendMessage message = new SendMessage();
        Test javaTest = userService.getUser(update).getTest();
        String input = update.hasMessage() ? update.getMessage().getText() : update.getCallbackQuery().getData();

        switch (input) {
            case "Data Types", "Program Flow", "Object‐Oriented Approach", "Exception Handling",
                    "Arrays and Collections", "Streams and Lambda", "Platform Module System", "Concurrency", "I/O API",
                    "Secure Coding", "Database with JDBC", "Localization", "Annotations", "Practice Exam 1",
                    "Practice Exam 2", "Practice Exam 3" -> {
                javaTest.setSelectedChapter(commands.indexOf(input));
                testRepo.save(javaTest);
                sendFirstQuestion(btnManager, message, update, javaTest);
            }
            case "Oracle prob" -> sendChapters(btnManager, message, update);

        }

        return message;
    }

    private void sendFirstQuestion(BtnManager btnManager, SendMessage message, Update update, Test javaTest) {
        Question question = chapters.get(javaTest.getSelectedChapter()).getQuestions().get(0);
        message.setReplyMarkup(btnManager.setInlineKeyboardMarkup(
                new String[][]{question.getRsp_id().toArray(String[]::new), {"◀", "Progress", "▶"}}));
        message.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        message.enableHtml(true);
        message.setText(question.getQuestion());
    }

    private void sendChapters(BtnManager btnManager, SendMessage message, Update update) {
        String[][] chapters_name = new String[16][1];

        for (int i = 0; i < chapters.size(); i++) {
            chapters_name[i][0] = chapters.get(i).getChapter();
        }

        message.setReplyMarkup(btnManager.setInlineKeyboardMarkup(chapters_name));
        message.setChatId(String.valueOf(update.hasMessage() ? update.getMessage().getChatId() :
                update.getCallbackQuery().getMessage().getChatId()));
        message.setText("Choose topic");
    }

    @Override
    public String[] getInputCommand() {
        return new String[]{"Data Types", "Program Flow", "Object‐Oriented Approach", "Exception Handling",
                "Arrays and Collections", "Streams and Lambda", "Platform Module System",
                "Concurrency", "I/O API", "Secure Coding", "Database with JDBC", "Localization",
                "Annotations", "Practice Exam 1", "Practice Exam 2", "Practice Exam 3", "Oracle prob"};
    }
}