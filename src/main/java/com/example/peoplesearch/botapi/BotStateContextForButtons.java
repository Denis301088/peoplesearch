package com.example.peoplesearch.botapi;

import com.example.peoplesearch.botapi.handlers.fillingprofile.UserProfileData;
import com.example.peoplesearch.cache.UserDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BotStateContextForButtons implements BotStateContext<InputButtonsHandler,CallbackQuery> {


    private Map<BotState, InputButtonsHandler> buttonsHandlers = new ConcurrentHashMap<>();
    private UserDataCache userDataCache;

    public BotStateContextForButtons(List<InputButtonsHandler> messageHandlers,UserDataCache userDataCache) {
        messageHandlers.forEach(handler -> this.buttonsHandlers.put(handler.getHandlerName(), handler));
        this.userDataCache=userDataCache;
    }

    @Override
    public BotApiMethod<?> processInput(BotState currentState, CallbackQuery button) {

//        if(!(userDataCache.getUserProfilesData(button.getFrom().getId(),button.getData())==null && currentState.equals(BotState.ASK_SURNAME))){
//
//            return buttonsHandlers.get(BotState.RESPONSE_USER).handle(button);
//        }
//        InputButtonsHandler inputButtonsHandler=buttonsHandlers.get(currentState);
//        return inputButtonsHandler==null? null:inputButtonsHandler.handle(button);

        UserProfileData userProfileData=userDataCache.getUserProfilesData(button.getFrom().getId(),button.getData());
        if (currentState.equals(BotState.ASK_SURNAME)){
            return buttonsHandlers.get(BotState.ASK_SURNAME).handle(button);
        }else if(userProfileData!=null){
            return buttonsHandlers.get(BotState.RESPONSE_USER).handle(button);
        }
//        else if(currentState.equals(BotState.RESPONSE_USER)){
//            return buttonsHandlers.get(BotState.RESPONSE_USER).handle(button);
//        }
        return null;
//        InputButtonsHandler inputButtonsHandler=buttonsHandlers.get(currentState);
//        return inputButtonsHandler==null? null:inputButtonsHandler.handle(button);
    }

    @Override
    public Map<BotState, InputButtonsHandler> getHandlers() {
        return buttonsHandlers;
    }
}
