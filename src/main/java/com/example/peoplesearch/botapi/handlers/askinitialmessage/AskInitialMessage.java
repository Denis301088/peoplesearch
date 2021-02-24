package com.example.peoplesearch.botapi.handlers.askinitialmessage;

import com.example.peoplesearch.botapi.BotState;
import com.example.peoplesearch.botapi.InputMessageHandler;
import com.example.peoplesearch.cache.UserDataCache;
import com.example.peoplesearch.domain.PeopleSearchBot;
import com.example.peoplesearch.service.MainMenuService;
import com.example.peoplesearch.service.ReplyMessagesService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class AskInitialMessage implements InputMessageHandler {

    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private MainMenuService mainMenuService;

    public AskInitialMessage(UserDataCache userDataCache, ReplyMessagesService messagesService, MainMenuService mainMenuService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public void setPeopleSearchBot(PeopleSearchBot peopleSearchBot) {

    }

    @Override
    public BotApiMethod<?> handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.INITIAL_MESSAGE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
//        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        SendMessage replyToUser = messagesService.getReplyMessage(chatId,"reply.askInitialMessage");
//        userDataCache.setUsersCurrentBotState(userId, BotState.INITIAL_MESSAGE);
        replyToUser.setReplyMarkup(mainMenuService.getMainMenuKeyboard());
        return replyToUser;
    }

}
