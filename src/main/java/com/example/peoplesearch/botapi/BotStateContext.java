package com.example.peoplesearch.botapi;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

public interface BotStateContext<Handler,Input> {

    BotApiMethod<?> processInput(BotState currentState, Input input);

    Map<BotState, Handler> getHandlers();
}
