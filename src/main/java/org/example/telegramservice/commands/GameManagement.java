package org.example.telegramservice.commands;

import org.example.telegramservice.dbservice.game.Match;
import org.example.telegramservice.dbservice.repository.MatchRepo;
import org.example.telegramservice.service.BtnManager;
import org.example.telegramservice.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class GameManagement implements CommandGenerator<EditMessageText> {

    private final String[] serving = new String[]{"\uD83C\uDFD3", ""};
    private Match match;

    private final UserService userService;
    private final MatchRepo matchRepo;

    public GameManagement(UserService userService, MatchRepo matchRepo) {
        this.userService = userService;
        this.matchRepo = matchRepo;
    }

    @Override
    public EditMessageText generate(Update update) {
        String gameState = getGameState(update.getCallbackQuery().getData());
        EditMessageText message = new EditMessageText();
        match = userService.getOpponent(update);

        switch (gameState) {
            case "opponentWon" -> messageSetup(message, update, match.getOpponentName() + " won");
            case "userWon" -> messageSetup(message, update, update.getCallbackQuery().getFrom().getFirstName() + " won");
            case "new game" -> {
                newGame(update);
                messageSetup(message, update, "board");
            }
            default -> messageSetup(message, update, "board");
        }

        return message;
    }

    @Override
    public String[] getInputCommand() {
        return new String[]{"1.+", "2.+", "new game", "↔"};
    }

    private String getGameState(String input) {
        int opponent_score = match.getOpponentScore();
        int opponent_pt = match.getOpponentPts();
        int my_score = match.getMyScore();
        int my_pt = match.getMyPts();
        boolean end_game = false;

        switch (input) {
            case "1.+":
                match.setMyPts(++my_pt);
                break;
            case "2.+":
                match.setOpponentPts(++opponent_pt);
                break;
            case "↔":
                changeServingPrs(true);
                return "default";
            default:
                return "new game";
        }
        changeServingPrs(false);

        if ((my_pt > 10 || opponent_pt > 10) && Math.abs(opponent_pt - my_pt) > 1) {
            match.setOpponentPts(0);
            match.setMyPts(0);
            if (input.equals("1.+"))
                match.setMyScore(++my_score);
            else
                match.setOpponentScore(++opponent_score);

            if (my_score == 2 || opponent_score == 2) {
                match.setMyScore(0);
                match.setOpponentPts(0);
                end_game = true;
            }
        }
        matchRepo.save(match);
        return end_game ? input.equals("1.+") ? "userWon" : "opponentWon" : "default";
    }

    private void newGame(Update update) {
        match = userService.getOpponent(update);
        match.setOpponentScore(0);
        match.setOpponentPts(0);
        match.setMyScore(0);
        match.setMyPts(0);
        matchRepo.save(match);
    }

    private void changeServingPrs(boolean changeCommand) {
        if (Math.abs(match.getMyPts() - match.getOpponentPts()) % 2 == 0 || changeCommand) {
            serving[0] = serving[0].equals("") ? "\uD83C\uDFD3" : "";
            serving[1] = serving[1].equals("") ? "\uD83C\uDFD3" : "";
        }
    }

    private void messageSetup(EditMessageText message, Update update, String text) {
        String board = String.format("%s|%s|%s|%s\n%s|%s|%s|%s",
                "1. " + update.getCallbackQuery().getFrom().getFirstName(), match.getMyPts(), match.getMyScore(), serving[0],
                "2. " + match.getOpponentName(), match.getOpponentPts(), match.getOpponentScore(), serving[1]);
        BtnManager btnManager = new BtnManager();

        message.setReplyMarkup(btnManager.setInlineKeyboardMarkup(new String[][]{{"1.+", "2.+", "↔", "new game"}}));
        message.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        if (text.equals("board")) message.setText(board);
        else message.setText(text);
    }
}