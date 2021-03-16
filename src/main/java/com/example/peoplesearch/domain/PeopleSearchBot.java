package com.example.peoplesearch.domain;

import com.example.peoplesearch.botapi.TelegramFacade;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public class PeopleSearchBot extends TelegramWebhookBot {


    private String botPath;
    private String botUsername;
    private String botToken;

    private TelegramFacade telegramFacade;


    public PeopleSearchBot(TelegramFacade telegramFacade, DefaultBotOptions options) {

        this.telegramFacade = telegramFacade;
    }

    //1)
    public TelegramFacade getTelegramFacade() {
        return telegramFacade;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        return telegramFacade.handleUpdate(update);
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void setBotPath(String botPath) {
        this.botPath = botPath;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }
}
