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
import pro.sky.telegrambot.service.impl.CityServiceImpl;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final CityServiceImpl citiesService;
    private Cities listOfCities;
    String lastChar;
    int easyCount = 100;
    int normalCount = 250;
    int hardCount = 500;


    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(CityServiceImpl citiesService) {
        this.citiesService = citiesService;
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
                Long chatId = update.message().chat().id();
                switch (msg) {
                    case "/newgame легко":
                        startNewGame(chatId, easyCount);
                        break;
                    case "/newgame средне":
                        startNewGame(chatId, normalCount);
                        break;
                    case "/newgame сложно":
                        startNewGame(chatId, hardCount);
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
                        if (listOfCities == null) {
                            sendMessage(chatId, "Начни новую игру командой /newgame и выбранный уровень сложности, например\n" +
                                    "/newgame легко");
                        } else if (!citiesService.checkIsCityExistInDB(msg)) {
                            sendMessage(chatId, "Такого города не существует.");
                        } else if (!listOfCities.getLastCity().equals("") && listOfCities.getLastCity().charAt(listOfCities.getLastCity().length() - 1) != Character.toLowerCase(msg.charAt(0))) {
                            char lastChar = listOfCities.getLastCity().charAt(listOfCities.getLastCity().length() -  1);
                            sendMessage(chatId, "Нужно начинать с последней буквы предыдущего города. В нашем случае, это " + lastChar);
                        } else if (listOfCities.getListOfUsedCities().contains(msg)) {
                            sendMessage(chatId, "Такой город уже был использован.");
                        } else {
                            //Город есть в игровом списке?
                            listOfCities.getListOfCities().remove(msg);
                            listOfCities.addNewCityToUsedList(msg); //Добавляем город в список использованных.
                            lastChar = lastChar(msg);
                            List<String> newCityList = listOfCities.getListOfCities().stream()
                                    .filter(e -> e.startsWith(lastChar))
                                    .limit(1)
                                    .collect(Collectors.toList());
                            if (!newCityList.isEmpty()) { //Нашли город?
                                String newCity = newCityList.get(0);
                                listOfCities.getListOfCities().remove(newCity); //Удаляем новый город из игрового списка.
                                logger.info("Удаляем новый город " + newCity + " из игрового списка.");
                                listOfCities.getListOfUsedCities().add(newCity); //Добавляем новый город в список использованных.
                                logger.info("Добавляем новый город " + newCity + " в список использованных.");
                                listOfCities.setLastCity(newCity); //Добавляем новый город, как последний.
                                logger.info("Добавляем новый город " + newCity + ", как последний.");
                                lastChar = lastChar(newCity);
                                sendMessage(chatId, newCity + ", тебе на \"" + lastChar.toUpperCase() + "\"");
                            } else {
                                String imageFile = "https://c.tenor.com/j_ijiBkU2a8AAAAM/title-victory.gif";
                                SendSticker sendSticker = new SendSticker(chatId, imageFile);
                                telegramBot.execute(sendSticker);
                                logger.info(String.valueOf(listOfCities));
                                listOfCities.getListOfCities().clear();
                                listOfCities.getListOfUsedCities().clear();
                                listOfCities.setLastCity("");
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

    private void startNewGame(long chatId, int count) {
        listOfCities = citiesService.makeNewListOfCities(count, chatId);
        sendMessage(chatId, "Я создал игру и вспомнил лишь " + count + " городов. Начинай первый!");
        logger.info(String.valueOf(listOfCities));
    }

    private String lastChar(String msg) {
        if ((msg.charAt(msg.length() - 1) != 'ь') && (msg.charAt(msg.length() - 1) != 'ы')) {
            return String.valueOf(msg.charAt(msg.length() - 1));
        } else return String.valueOf(msg.charAt(msg.length() - 2));
    }
}
