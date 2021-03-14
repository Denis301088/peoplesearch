package com.example.peoplesearch.botapi.handlers.buttonshandler;

import com.example.peoplesearch.botapi.BotState;
import com.example.peoplesearch.botapi.InputButtonsHandler;
import com.example.peoplesearch.botapi.InputMessageHandler;
import com.example.peoplesearch.botapi.handlers.fillingprofile.UserProfileData;
import com.example.peoplesearch.cache.UserDataCache;
import com.example.peoplesearch.domain.PeopleSearchBot;
import com.example.peoplesearch.service.ReplyMessagesService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.ArrayList;
import java.util.List;


/*
* Получение данных поиска
* */
@Component
public class ButtonDataRetrieval implements InputButtonsHandler {

    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private PeopleSearchBot peopleSearchBot;

    public ButtonDataRetrieval(UserDataCache userDataCache, ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public void setPeopleSearchBot(PeopleSearchBot peopleSearchBot) {
        this.peopleSearchBot=peopleSearchBot;
    }

    @Override
    public BotApiMethod<?> handle(CallbackQuery callbackQuery) {

        int userId = callbackQuery.getFrom().getId();
        long chatId = callbackQuery.getMessage().getChatId();
//        UserProfileData profileData=userDataCache.getUserProfileData(userId);
//        BotState botState = userDataCache.getUsersCurrentBotState(userId);
        UserProfileData userProfileDataFromButtonId=userDataCache.getUserProfilesData(callbackQuery.getFrom().getId(),callbackQuery.getData());


        if(userProfileDataFromButtonId.isAnswerFormed()){

            DeleteMessage delete = new DeleteMessage();
            delete.setChatId(String.valueOf(chatId));

            delete.setMessageId(callbackQuery.getMessage().getMessageId());
            try {
                peopleSearchBot.execute(delete);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            SendMessage replyToUser=new SendMessage(String.valueOf(chatId), String.format("Ответ по вашему запросу:\n\n%s",userProfileDataFromButtonId.getData()));
            userDataCache.setUsersCurrentBotState(userId,BotState.INITIAL_MESSAGE);
            userDataCache.removeUserProfileData(userId);
            userDataCache.removeUserProfilesData(userId,callbackQuery.getData());
            return replyToUser;
        }

        AnswerCallbackQuery answerCallbackQuery=new AnswerCallbackQuery();
//            answerCallbackQuery.setCacheTime(10);
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setShowAlert(false);
        answerCallbackQuery.setText("Запрос еще не обработан");


        return answerCallbackQuery;

    }


    @Override
    public BotState getHandlerName() {
        return BotState.RESPONSE_USER;
    }
}
