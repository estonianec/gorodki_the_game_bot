package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendSticker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Cities;
import pro.sky.telegrambot.model.Rating;
import pro.sky.telegrambot.service.impl.CityServiceImpl;
import pro.sky.telegrambot.service.impl.RatingServiceImpl;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final CityServiceImpl citiesService;
    private final RatingServiceImpl ratingService;
    String lastChar;
    int easyCount = 100;
    int normalCount = 250;
    int hardCount = 500;
    int testCount = 10;
    long chatId;
    HashMap<Long, Cities> session = new HashMap<>();
    Cities currentSession;


    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(CityServiceImpl citiesService, RatingServiceImpl ratingService) {
        this.citiesService = citiesService;
        this.ratingService = ratingService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here
            if (update.message() != null && update.message().text() != null) {
                String msg = update.message().text();
                chatId = update.message().chat().id();
                String name = update.message().from().firstName();
                switch (msg) {
                    case "/newgame легко":
                        startNewGame(chatId, easyCount, name);
                        break;
                    case "/newgame средне":
                        startNewGame(chatId, normalCount, name);
                        break;
                    case "/newgame сложно":
                        startNewGame(chatId, hardCount, name);
                        break;
                    case "/newgame тест":
                        startNewGame(chatId, testCount, name);
                        break;
                    case "/rating":
                        List<Rating> rating = ratingService.getRating();
                        Rating userRating = ratingService.getUserRating(chatId);
                        sendMessage(chatId, "Ты набрал " + userRating.getPoints() + " очков в " + userRating.getGames() + " игре.\n\n" +
                                "Топ игроков:\n" +
                                "\uD83E\uDD47 - " + rating.get(0).getName() + " - " + rating.get(0).getPoints() + " очков и " + rating.get(0).getGames() + " игр\n" +
                                "\uD83E\uDD48 - " + rating.get(1).getName() + " - " + rating.get(1).getPoints() + " очков и " + rating.get(1).getGames() + " игр\n" +
                                "\uD83E\uDD49 - " + rating.get(2).getName() + " - " + rating.get(2).getPoints() + " очков и " + rating.get(2).getGames() + " игр\n");
                        break;
                    case "/start":
                        logger.info("start");
                        sendMessage(chatId, "Привет! Здесь ты можешь поиграть в старые, добрые \"Города\"! \n" +
                                "Я знаю " + citiesService.getCountOfCitiesInDB() + " уникальных городов, но не переживай - я буду использовать количество городов в соответствии с выбранным тобой уровнем сложности:\n" +
                                "- легко - " + easyCount + " городов;\n" +
                                "- средне - " + normalCount + " городов;\n" +
                                "- сложно - " + hardCount + " городов;\n" +
                                "Начни новую игру командой /newgame и выбранный уровень сложности, например\n" +
                                "/newgame легко");
                        break;
                    default:
                        logger.info(msg);
                        currentSession = session.get(chatId);
                        if (currentSession == null || currentSession.getListOfCities().isEmpty()) {
                            sendMessage(chatId, "Начни новую игру командой /newgame и выбранный уровень сложности, например\n" +
                                    "/newgame легко");
                        } else if (!citiesService.checkIsCityExistInDB(msg)) {
                            sendMessage(chatId, "Такого города не существует.");
                        } else if (!currentSession.getLastCity().equals("") && !lastChar(currentSession.getLastCity()).equals(String.valueOf(Character.toLowerCase(msg.charAt(0))))) {
                            lastChar = lastChar(currentSession.getLastCity());
                            sendMessage(chatId, "Нужно начинать с последней буквы предыдущего города. В нашем случае, это " + lastChar);
                        } else if (currentSession.getListOfUsedCities().contains(msg)) {
                            sendMessage(chatId, "Такой город уже был использован.");
                        } else {
                            //Город есть в игровом списке?
                            currentSession.getListOfCities().remove(msg);
                            currentSession.addNewCityToUsedList(msg); //Добавляем город в список использованных.
                            lastChar = lastChar(msg);
                            List<String> newCityList = currentSession.getListOfCities().stream()
                                    .filter(e -> e.startsWith(lastChar))
                                    .limit(1)
                                    .collect(Collectors.toList());
                            if (!newCityList.isEmpty()) { //Нашли город?
                                String newCity = newCityList.get(0);
                                currentSession.getListOfCities().remove(newCity); //Удаляем новый город из игрового списка.
                                logger.info("Удаляем новый город " + newCity + " из игрового списка.");
                                currentSession.getListOfUsedCities().add(newCity); //Добавляем новый город в список использованных.
                                logger.info("Добавляем новый город " + newCity + " в список использованных.");
                                currentSession.setLastCity(newCity); //Добавляем новый город, как последний.
                                logger.info("Добавляем новый город " + newCity + ", как последний.");
                                lastChar = lastChar(newCity);
                                sendMessage(chatId, newCity + ", тебе на \"" + lastChar.toUpperCase() + "\"");
                            } else {
                                String imageFile = "https://c.tenor.com/j_ijiBkU2a8AAAAM/title-victory.gif";
                                SendSticker sendSticker = new SendSticker(chatId, imageFile);
                                telegramBot.execute(sendSticker);
                                logger.info(String.valueOf(currentSession));
                                currentSession.getListOfCities().clear();
                                ratingService.increasePoints(chatId, currentSession.getListOfUsedCities().size());
                                currentSession.getListOfUsedCities().clear();
                                currentSession.setLastCity("");
                                sendMessage(chatId, "Победа за тобой! Мне не удалось вспомнить город на букву \"" + lastChar.toUpperCase() + "\"\n\n" +
                                        "Для новой игры введи команду /newgame и выбранный уровень сложности, например\n" +
                                        "/newgame легко");}
                        }
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendMessage(long chatId, String msgToSend) {
        SendMessage request = new SendMessage(chatId, msgToSend);
        telegramBot.execute(request);
    }

    private void startNewGame(long chatId, int count, String name) {
        Cities listOfCities = citiesService.makeNewListOfCities(count, chatId);
        session.put(chatId, listOfCities);
        if (ratingService.isUserExist(chatId)) {
            ratingService.increaseGameCount(chatId);
        } else ratingService.createNewUser(chatId, name);
        sendMessage(chatId, "Я создал игру и вспомнил лишь " + count + " городов. Начинай первый!");
        logger.info(String.valueOf(listOfCities));
    }

    private String lastChar(String msg) {
        if ((msg.charAt(msg.length() - 1) != 'ь') && (msg.charAt(msg.length() - 1) != 'ы')) {
            return String.valueOf(msg.charAt(msg.length() - 1));
        } else return String.valueOf(msg.charAt(msg.length() - 2));

    }
}
