package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.Rating;

import java.util.List;

public interface RatingService {
    boolean isUserExist(long chatId);

    void increaseGameCount(long chatId);

    void createNewUser(long chatId, String name);

    void increasePoints(long chatId, int points);

    List<Rating> getRating();

    Rating getUserRating(long chatId);

    List<Rating> getListForSpam();
}
