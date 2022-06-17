package org.example.telegramservice.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import org.example.telegramservice.dbservice.entity.Test;
import org.example.telegramservice.dbservice.entity.User;
import org.example.telegramservice.dbservice.repository.TestRepo;
import org.example.telegramservice.service.BtnManager;
import org.example.telegramservice.service.ChConfig.Question;
import org.example.telegramservice.service.ChConfig.TestsConfig;
import org.example.telegramservice.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.List;

@Component
public class OracleTestManagement implements CommandGenerator<EditMessageText> {

    Question question;

    private final UserService userService;
    private final TestRepo testRepo;

    public OracleTestManagement(UserService userService, TestRepo testRepo) {
        this.userService = userService;
        this.testRepo = testRepo;
    }

    @Override
    public EditMessageText generate(Update update) {
        User user = userService.getUser(update);
        Test test = user.getTest();
        List<Question> questions = getQuestions(test.getSelectedChapter());
        String input = update.getCallbackQuery().getData();
        StringBuilder text = new StringBuilder();
        question = questions.get(test.getCurrPrb());

        text.append(switch (input) {
            case "Progress" -> addProgressInfo(new StringBuilder(), test);
            case "A", "B", "C", "D", "F", "E" -> evaluateResponse(input, test);
            case "▶", "◀" -> changeQuestion(input, questions, user, test);
            case "\uD83D\uDCA1" -> "\n\uD83D\uDC40 " + question.getExplication() + "\n\n";
            default -> "";
        }).append(question.getQuestion()).append("\n");

        return setText(update, text);
    }

    private String addProgressInfo(StringBuilder text, Test test) {
        return text
                .append("✅ answers: ").append(test.getRightAns()).append("\n")
                .append("❌ answers: ").append(test.getWrongAns()).append("\n")
                .append("questions left: ").append(test.getPrbLeft()).append("\n\n").toString();
    }

    private String changeQuestion(String input, List<Question> questions, User user, Test test) {
        if (input.equals("▶"))
            if (test.getPrbLeft() == 0 && test.getCurrPrb() == 6)
                return "That's last question.\n\n";
            else if (test.getRightAns() > test.getCurrPrb())
                test.setCurrPrb(test.getCurrPrb() + 1);
            else
                return "\uD83D\uDEA8 You have to finish this question to get further\n\n";
        else if (test.getCurrPrb() == 0)
            return "\uD83D\uDEA8 That's first question\n\n";
        else
            test.setCurrPrb(test.getCurrPrb() - 1);

        question = questions.get(test.getCurrPrb());
        testRepo.save(user.getTest());

        return "";
    }

    private String evaluateResponse(String input, Test test) {
        StringBuilder response = new StringBuilder();

        if (question.getAnswer().contains(input)) {
            if (test.getRightAns() == test.getCurrPrb()) {
                test.setRightAns(test.getRightAns() + 1);
                test.setPrbLeft(test.getPrbLeft() - 1);
            }
            response.append("\n✅ Correct\n\n");
        } else {
            test.setWrongAns(test.getWrongAns() + 1);
            response.append("\n\uD83D\uDEA8 Response ").append(input).append(" is incorrect\n\n");
        }
        testRepo.save(test);

        return response.toString();
    }

    @Override
    public String[] getInputCommand() {
        return new String[]{"A", "B", "C", "D", "E", "F", "\uD83D\uDCA1",
                "◀", "▶", "Progress"};
    }

    @SneakyThrows
    private List<Question> getQuestions(int chapter_id) {
        File file = new File("src/main/resources/java_test.yaml");
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        TestsConfig testsConfig = objectMapper.readValue(file, TestsConfig.class);

        return testsConfig.getChapters().get(chapter_id).getQuestions();
    }

    private EditMessageText setText(Update update, StringBuilder text) {
        EditMessageText message = new EditMessageText();
        BtnManager btnManager = new BtnManager();

        message.setReplyMarkup(btnManager.setInlineKeyboardMarkup(
                new String[][]{question.getRsp_id().toArray(String[]::new), {"◀", "Progress", "▶"}}));
        message.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        message.setText(text.toString());

        return message;
    }
}