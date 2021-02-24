package com.example.peoplesearch.botapi;

import com.example.peoplesearch.domain.PeopleSearchBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface InputButtonsHandler {

     void setPeopleSearchBot(PeopleSearchBot peopleSearchBot);

     BotApiMethod<?> handle(CallbackQuery callbackQuery);

    BotState getHandlerName();
}
