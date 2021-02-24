package com.example.peoplesearch.botapi;

import com.example.peoplesearch.domain.PeopleSearchBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Обработчики сообщений для каждого состояния.
 */

@Component
public class BotStateContextForMessages implements BotStateContext<InputMessageHandler,Message>{

    private Map<BotState, InputMessageHandler> messageHandlers = new ConcurrentHashMap<>();


    public BotStateContextForMessages(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public Map<BotState, InputMessageHandler> getHandlers() {
        return messageHandlers;
    }

    @Override
    public BotApiMethod<?> processInput(BotState currentState, Message message) {

        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        //2)После того как получили состояние,вызываем его обработку методом handle
        return currentMessageHandler==null?null:currentMessageHandler.handle(message);
    }

//    public BotApiMethod<?> processInputMessage(BotState currentState, CallbackQuery callbackQuery) {
//        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
//        //2)После того как получили состояние,вызываем его обработку методом handle
//        return currentMessageHandler.handle(callbackQuery);
//    }

    private InputMessageHandler findMessageHandler(BotState currentState) {

        if(currentState.equals(BotState.MENU_INFO)||currentState.equals(BotState.MENU_HELP)){
            return messageHandlers.get(BotState.MENU);
        }if (isFillingProfileState(currentState)) {
            //1) Проверяю,если состояние установлено в PROFILE_FILLED,тоесть заполнение
            // анкеты,то возвращаем обработчик состояния из Map<BotState, InputMessageHandler> messageHandlers,
            // соответствущий FILLING_PROFILE
            return messageHandlers.get(BotState.FILLING_PROFILE);
        }
        //иначе возвращаем,тот обработчик,который относится к текущему BotState currentState
        return messageHandlers.get(currentState);
    }

    private boolean isFillingProfileState(BotState currentState) {
        switch (currentState) {
            case ASK_NAME:
            case ASK_PATRONYMIC:
            case FILLING_PROFILE:
            case PROFILE_FILLED:
                return true;
            default:
                return false;
        }
    }
}
