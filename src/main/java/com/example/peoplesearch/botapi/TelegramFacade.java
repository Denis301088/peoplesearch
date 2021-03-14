package com.example.peoplesearch.botapi;


import com.example.peoplesearch.cache.UserDataCache;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TelegramFacade {

    private BotStateContextForMessages botStateContextForMessages;
    private BotStateContextForButtons botStateContextForButtons;
    private UserDataCache userDataCache;

    public TelegramFacade(BotStateContextForMessages botStateContextForMessages,
                          BotStateContextForButtons botStateContextForButtons,
                          UserDataCache userDataCache) {
        this.botStateContextForMessages = botStateContextForMessages;
        this.botStateContextForButtons = botStateContextForButtons;
        this.userDataCache = userDataCache;
    }

    public BotStateContextForMessages getBotStateContextForMessages() {
        return botStateContextForMessages;
    }

    public BotStateContextForButtons getBotStateContextForButtons() {
        return botStateContextForButtons;
    }

    public BotApiMethod<?> handleUpdate(Update update) {

        BotApiMethod<?> replyMessage = null;

        if (update.hasCallbackQuery()) {

            CallbackQuery buttonQuery = update.getCallbackQuery();

            return botStateContextForButtons.processInput(userDataCache.getUsersCurrentBotState(buttonQuery.getFrom().getId()),buttonQuery);

        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {

            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private BotApiMethod<?> handleInputMessage(Message message) {

        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        BotState botState;
        BotApiMethod<?> replyMessage;

        switch (inputMsg) {
            case "/start":
                botState = BotState.INITIAL_MESSAGE;
                break;
            case "Получить данные по ФИО":
                botState = BotState.FILLING_PROFILE;
                break;
            case "Информация":
                botState = BotState.MENU_INFO;
                break;
            case "Помощь":
                botState = BotState.MENU_HELP;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;

        }

        userDataCache.setUsersCurrentBotState(userId, botState);//Устанавливаем состояние,которое мы получили в результате
        //обработки в switch

        replyMessage = botStateContextForMessages.processInput(botState, message);//передаем это состояние на обработку
        //обработчику состояния

//        replyMessage.setReplyMarkup(mainMenuService.getMainMenuKeyboard());
        return replyMessage;
    }
}


