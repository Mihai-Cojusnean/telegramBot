package org.example.telegramservice.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.example.telegramservice.dbservice.entity.User;
import org.example.telegramservice.dbservice.repository.UserRepo;
import org.example.telegramservice.service.BtnManager;
import org.example.telegramservice.service.UserService;
import org.example.telegramservice.service.oracleTestConfig.OracleProbConfig;
import org.example.telegramservice.service.oracleTestConfig.Problem;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.List;

@Component
public class OracleTestManagement implements CommandGenerator<EditMessageText> {
    private final UserService userService;
    private final UserRepo userRepo;

    public OracleTestManagement(UserService userService, UserRepo userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
    }

    @Override
    public EditMessageText generate(Update update) {
        int messageId = update.getCallbackQuery().getMessage().getMessageId();
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        String input = update.getCallbackQuery().getData();
        User user = userService.getUser(update, chatId);
        EditMessageText message = new EditMessageText();
        StringBuilder text = new StringBuilder();

        message.setMessageId(messageId);
        message.setChatId(chatId);

        switch (input) {
            case "A", "B", "C", "D", "F", "E" -> evaluateResponse(text, input, user);
            case "▶", "◀" -> changeProblem(input, user);
        }

        setUpText(message, text, input, user);
        return message;
    }

    private void changeProblem(String dir, User user) {
        if (dir.equals("▶")) user.setCurrProblem(user.getCurrProblem() + 1);
        else user.setCurrProblem(user.getCurrProblem() - 1);
        userRepo.save(user);
    }

    private void evaluateResponse(StringBuilder text, String input, User user) {
        Problem problem = getProblem(user);
        if (input.equals(problem.getAnswer()))
            text.append("\n\uD83D\uDEA8 Correct\n");
        else text.append("\n\uD83D\uDEA8 Incorrect\n");
    }

    @Override
    public String[] getInputCommand() {
        return new String[]{"A", "B", "C", "D", "E", "F", "\uD83D\uDCA1",
                "◀", "▶",
                "Java Data Types", "Program Flow"};
    }

    private Problem getProblem(User user) {
        File file = new File("src/main/resources/oracle_test.yaml");
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        Problem problem = new Problem();
        try {
            OracleProbConfig config = objectMapper.readValue(file, OracleProbConfig.class);
            List<Problem> problems = config.getProblems();
            problem = problems.get(user.getCurrProblem());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return problem;
    }

    private void setUpText(EditMessageText message, StringBuilder text, String input, User user) {
        BtnManager btnManager = new BtnManager();
        Problem problem = getProblem(user);

        if (input.equals("\uD83D\uDCA1")) text.append("a");
        else text.append(problem.getQuestion()).append("\n").append(problem.getResponses());

        message.setReplyMarkup(btnManager.setInlineKeyboardMarkup(new String[][]{problem.getResponse_id().toArray(String[]::new), {"◀", "▶"}}));
        message.setText(text.toString());
    }
}
