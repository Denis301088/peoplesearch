package com.example.peoplesearch.botapi;


import com.example.peoplesearch.domain.PeopleSearchBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Интерфейс для обработчиков сообщений
 */
public interface InputMessageHandler {

    void setPeopleSearchBot(PeopleSearchBot peopleSearchBot);

    BotApiMethod<?> handle(Message message);

    BotState getHandlerName();
}
