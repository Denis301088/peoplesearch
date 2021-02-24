package com.example.peoplesearch.botapi.handlers.fillingprofile;

import com.example.peoplesearch.botapi.BotState;
import com.example.peoplesearch.botapi.InputMessageHandler;
import com.example.peoplesearch.cache.UserDataCache;
import com.example.peoplesearch.domain.PeopleSearchBot;
import com.example.peoplesearch.service.DataRetrievalService;
import com.example.peoplesearch.service.ReplyMessagesService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

@Component
public class FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private DataRetrievalService dataRetrievalService;//Добавил асинхронный сервис

    public FillingProfileHandler(UserDataCache userDataCache, ReplyMessagesService messagesService, DataRetrievalService dataRetrievalService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.dataRetrievalService = dataRetrievalService;
    }

    @Override
    public void setPeopleSearchBot(PeopleSearchBot peopleSearchBot) {

    }

    @Override
    public BotApiMethod<?> handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_CITY);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }


    private BotApiMethod<?> processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_CITY)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askСity");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_SURNAME);
            replyToUser.setReplyMarkup(getButtonsMarkupForCity());
            userDataCache.saveUserProfileData(userId, profileData);
        }

        if (botState.equals(BotState.ASK_NAME)) {
            profileData.setSurName(usersAnswer.trim());
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askName");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PATRONYMIC);
        }

        if (botState.equals(BotState.ASK_PATRONYMIC)) {
            profileData.setName(usersAnswer.trim());
            replyToUser = messagesService.getReplyMessage(chatId, "reply.patronymic");
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
        }

        if (botState.equals(BotState.PROFILE_FILLED)) {
            profileData.setPatronymic(usersAnswer.trim());
//            userDataCache.setUsersCurrentBotState(userId, BotState.INITIAL_MESSAGE);

            replyToUser = new SendMessage(String.valueOf(chatId), String.format("Запрошены данные для: \n\n%s: %s %s %s",profileData.getCity() ,profileData.getSurName(),profileData.getName(), profileData.getPatronymic()));
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            InlineKeyboardButton inlineKeyboardButton=new InlineKeyboardButton("Получить данные");
            inlineKeyboardButton.setCallbackData(UUID.randomUUID().toString());
            inlineKeyboardMarkup.setKeyboard(List.of(List.of(inlineKeyboardButton)));
            replyToUser.setReplyMarkup(inlineKeyboardMarkup);
            userDataCache.setUsersCurrentBotState(userId, BotState.RESPONSE_USER);
            userDataCache.setUserProfilesData(userId,inlineKeyboardButton.getCallbackData(),profileData);
            dataRetrievalService.execute(userId);


        }



        return replyToUser;
    }

    private InlineKeyboardMarkup getButtonsMarkupForCity(){

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        Iterator<String>iteratorCity=new LinkedHashSet<String>(){
            {
                add("Алтай");add("Архангельск");add("Астрахань");
                add("Белгород");add("Брянск");add("Бурятия");
                add("Волгоград");add("Воронеж");add("Владивосток");
                add("Донецк");add("Екатеринбург");add("Иваново-Франковск");
                add("Калининград");add("Киров");add("Краснодар");
                add("Москва");add("Мурманск");add("Нижний Тагил");
                add("Орел");add("Оренбург");add("Пенза");
                add("Ростов-на-Дону");add("Самара");add("Санкт-Петербург");
                add("Тула");add("Тюмень");add("Удмуртия");
                add("Харьков");add("Челябинск");add("Чита");
                add("Казахстан");add("Казань");add("Башкортостан");

            }
        }.iterator();

        Iterator<String>iteratorCityCallBackData=new LinkedHashSet<String>(){
            {
                add("altai");add("arhangelsk");add("astrahan");
                add("belgorod");add("bryansk");add("buryatia");
                add("volgograd");add("voronej");add("vladivostok");
                add("donetsk");add("ekaterinburg");add("ivanofrankovsk");
                add("kaliningrad");add("kirov");add("krasnodar");
                add("moskva");add("murmansk");add("tagil");
                add("orel");add("orenburg");add("penza");
                add("rostov");add("samara");add("piter");
                add("tula");add("tumen");add("udmurtia");
                add("harkov");add("chelyabinsk");add("chita");
                add("kazahstan");add("kazan");add("bashkortostan");

            }
        }.iterator();

//                add("Altay");add("Arhangelsk");add("Astrakhan");
//                add("Belgorod");add("Bryansk");add("Buryatiya");
//                add("Volgograd");add("Voronezh");add("Vladivostok");
//                add("Donetsk");add("Yekaterinburg");add("IvanovoFrankovsk");
//                add("Kaliningrad");add("Kirov");add("Krasnodar");
//                add("Moskva");add("Murmansk");add("NizhniyTagil");
//                add("Orel");add("Orenburg");add("Penza");
//                add("RostovNaDonu");add("Samara");add("SanktPeterburg");
//                add("Tula");add("Tyumen");add("Udmurtiya");
//                add("Kharkov");add("Chelyabinsk");add("Chita");
//                add("Kazakhstan");add("Kazan");add("Bashkortostan");

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        while (iteratorCity.hasNext()){

            List<InlineKeyboardButton>inlineKeyboardButtonLine=new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                String city=iteratorCity.next();
                InlineKeyboardButton inlineKeyboardButton=new InlineKeyboardButton(city);
                inlineKeyboardButton.setCallbackData(iteratorCityCallBackData.next());
                inlineKeyboardButtonLine.add(inlineKeyboardButton);
            }
            rowList.add(inlineKeyboardButtonLine);

        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    public UserDataCache getUserDataCache() {
        return userDataCache;
    }
}
