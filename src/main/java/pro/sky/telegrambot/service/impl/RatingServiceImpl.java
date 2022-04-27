package pro.sky.telegrambot.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Rating;
import pro.sky.telegrambot.repository.RatingRepository;
import pro.sky.telegrambot.service.RatingService;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;

    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public boolean isUserExist(long chatId) {
        return ratingRepository.getByChatId(chatId) != null;
    }

    @Override
    public void increaseGameCount(long chatId) {
    ratingRepository.increaseGameCount(chatId);
    }

    @Override
    public void createNewUser(long chatId, String name) {
    ratingRepository.insertNewUser(chatId,name);
    }

    @Override
    public void increasePoints(long chatId, int points) {
        ratingRepository.increasePoints(chatId, points);
    }

    @Override
    public List<Rating> getRating() {
        return ratingRepository.getRating();
    }

    @Override
    public Rating getUserRating(long chatId) {
        return ratingRepository.getByChatId(chatId);
    }

    @Override
    public List<Rating> getListForSpam() {
        return ratingRepository.getListForSpam();
    }
}
