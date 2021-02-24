package com.example.peoplesearch.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleMessageService {
    private final MessageSource messageSource;
    private final Locale locale=Locale.forLanguageTag("ru-RU");

    public LocaleMessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    public String getMessage(String message) {
        return messageSource.getMessage(message, null, locale);
    }

    public String getMessage(String message, Object... args) {
        return messageSource.getMessage(message, args, locale);
    }

}
