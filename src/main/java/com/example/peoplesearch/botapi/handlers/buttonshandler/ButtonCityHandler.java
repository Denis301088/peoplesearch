package com.example.peoplesearch.botapi.handlers.buttonshandler;

import com.example.peoplesearch.botapi.BotState;
import com.example.peoplesearch.botapi.InputButtonsHandler;
import com.example.peoplesearch.botapi.handlers.fillingprofile.UserProfileData;
import com.example.peoplesearch.cache.UserDataCache;
import com.example.peoplesearch.domain.PeopleSearchBot;
import com.example.peoplesearch.service.ReplyMessagesService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/*
* Обработка нажатия кнопки город
* */
@Component
public class ButtonCityHandler implements InputButtonsHandler {

    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private PeopleSearchBot peopleSearchBot;

    public ButtonCityHandler(UserDataCache userDataCache, ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public void setPeopleSearchBot(PeopleSearchBot peopleSearchBot) {

    }

    @Override
    public BotApiMethod<?> handle(CallbackQuery callbackQuery) {

        int userId = callbackQuery.getFrom().getId();
        long chatId = callbackQuery.getMessage().getChatId();
        UserProfileData profileData=userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        userDataCache.getUserProfileData(userId).setCity(callbackQuery.getData());

        List<InlineKeyboardButton> buttonList=new ArrayList<>();
        callbackQuery.getMessage().getReplyMarkup()
                .getKeyboard().forEach(list->buttonList.addAll(list));

        String city=buttonList.stream().filter(x->x.getCallbackData().equals(callbackQuery.getData())).findFirst().get().getText();
//            String city=callbackQuery.getMessage().getReplyMarkup()
//                    .getKeyboard().stream().map(list->list.stream().filter(x->x.getCallbackData()
//                    .equals(callbackQuery.getData())).findFirst().get().getCallbackData()).findFirst().get();
        UserProfileData userProfileData=userDataCache.getUserProfileData(userId);
        userProfileData.setCity(city);
        userProfileData.setCityFotBrowser(callbackQuery.getData());
        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.askSurName");
        userDataCache.setUsersCurrentBotState(userId, BotState.ASK_NAME);
        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_SURNAME;
    }
}
