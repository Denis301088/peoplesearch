package com.example.peoplesearch.botapi.handlers.menuhandler;

import com.example.peoplesearch.botapi.BotState;
import com.example.peoplesearch.botapi.InputMessageHandler;
import com.example.peoplesearch.botapi.handlers.fillingprofile.UserProfileData;
import com.example.peoplesearch.cache.UserDataCache;
import com.example.peoplesearch.domain.PeopleSearchBot;
import com.example.peoplesearch.service.ReplyMessagesService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MenuMessageHandler implements InputMessageHandler {

    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;

    public MenuMessageHandler(UserDataCache userDataCache, ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public void setPeopleSearchBot(PeopleSearchBot peopleSearchBot) {

    }

    @Override
    public BotApiMethod<?> handle(Message inputMsg) {
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.MENU_INFO)) {
            replyToUser = messagesService.getReplyMessage(chatId, "menu.info");
            userDataCache.saveUserProfileData(userId, profileData);
        }
        if(botState.equals(BotState.MENU_HELP)){
            replyToUser = messagesService.getReplyMessage(chatId, "menu.help");
            userDataCache.saveUserProfileData(userId, profileData);
        }
        userDataCache.setUsersCurrentBotState(userId, BotState.INITIAL_MESSAGE);
        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MENU;
    }


}
