package com.example.peoplesearch.appconfig;


import com.example.peoplesearch.botapi.BotState;
import com.example.peoplesearch.botapi.InputButtonsHandler;
import com.example.peoplesearch.botapi.InputMessageHandler;
import com.example.peoplesearch.botapi.TelegramFacade;
import com.example.peoplesearch.domain.PeopleSearchBot;
import com.example.peoplesearch.domain.UserBot;
import com.example.peoplesearch.repo.UserRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@Setter
@Getter
@Configuration
//@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {


    @Value("${telegrambot.userName}")
    private String userName;
    @Value("${telegrambot.botToken}")
    private String botToken;
    @Value("${telegrambot.webHookPath}")
    private String webHookPath;

    @Bean
    public PeopleSearchBot myPeopleSearchBot(TelegramFacade telegramFacade) {

        DefaultBotOptions options = new DefaultBotOptions();

        PeopleSearchBot peopleSearchBot = new PeopleSearchBot(telegramFacade,options);
        peopleSearchBot.setBotUsername(userName);
        peopleSearchBot.setBotToken(botToken);
        peopleSearchBot.setBotPath(webHookPath);
        for (Map.Entry<BotState,InputMessageHandler> inputMessageHandler:peopleSearchBot.getTelegramFacade().getBotStateContextForMessages().getHandlers().entrySet()){
            inputMessageHandler.getValue().setPeopleSearchBot(peopleSearchBot);
        }

        for (Map.Entry<BotState, InputButtonsHandler> inputButtonsHandler:peopleSearchBot.getTelegramFacade().getBotStateContextForButtons().getHandlers().entrySet()){
            inputButtonsHandler.getValue().setPeopleSearchBot(peopleSearchBot);
        }
        return peopleSearchBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public List<UserBot> allUsersDataBase(UserRepo userRepo) {

        return Collections.unmodifiableList(userRepo.findAll());

    }

}
