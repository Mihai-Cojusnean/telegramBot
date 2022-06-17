package org.example.telegramservice.service;

import org.example.telegramservice.dbservice.entity.Test;
import org.example.telegramservice.dbservice.entity.User;
import org.example.telegramservice.dbservice.game.Match;
import org.example.telegramservice.dbservice.repository.MatchRepo;
import org.example.telegramservice.dbservice.repository.UserRepo;
import org.example.telegramservice.dbservice.repository.TestRepo;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.util.Objects.isNull;

@Service
public class UserService {

    private final MatchRepo matchRepo;
    private final UserRepo userRepo;
    private final TestRepo TestRepo;

    public UserService(MatchRepo matchRepo, UserRepo userRepo, TestRepo testRepo) {
        this.matchRepo = matchRepo;
        this.userRepo = userRepo;
        TestRepo = testRepo;
    }

    /* user database */
    public User getUser(Update update) {
        User user = userRepo.findByChatId(update.hasCallbackQuery() ?
                update.getCallbackQuery().getMessage().getChatId().toString()
                : update.getMessage().getChatId().toString());

        if (isNull(user)) user = saveUser(update);

        return user;
    }

    public User saveUser(Update update) {
        return userRepo.save(new User(
                update.getMessage().getChatId().toString(),
                update.getMessage().getChat().getFirstName(),
                update.getMessage().getChat().getLastName(),
                update.getMessage().getFrom().getUserName(),
                update.getMessage().getFrom().getLanguageCode(),
                createTest()));
    }

    private Test createTest() {
        return TestRepo.save(new Test(
                0,
                0,
                1010,
                0,
                0
               ));
    }

    /* match database */
    public Match getOpponent(Update update) {
        Match match = matchRepo.findByChatId(update.hasCallbackQuery() ?
                        update.getCallbackQuery().getFrom().getId().toString()
                        : update.getMessage().getChatId().toString());

        if (isNull(match))
            match = saveOpponent(update);

        return match;
    }

    private Match saveOpponent(Update update) {
        return matchRepo.save(new Match(
                update.getMessage().getChatId().toString(),
                "Opponent",
                0,
                0,
                0,
                0));
    }
}