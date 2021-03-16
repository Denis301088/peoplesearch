package com.example.peoplesearch.controller;


import com.example.peoplesearch.domain.PeopleSearchBot;
import com.example.peoplesearch.domain.UserBot;
import com.example.peoplesearch.repo.UserRepo;
import com.example.peoplesearch.repo.UserRepoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

@RestController
public class WebHookController {

    private PeopleSearchBot telegrambot;

    private UserRepoService userRepoService;

    private UserRepo userRepo;

    public WebHookController(PeopleSearchBot telegrambot, UserRepoService userRepoService, UserRepo userRepo) {
        this.telegrambot = telegrambot;
        this.userRepoService = userRepoService;
        this.userRepo = userRepo;
    }

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {

        if (update.getMessage()!=null){
            User user=update.getMessage().getFrom();
            if(!userRepoService.isNewUser(user))
                userRepoService.updateUserDataBase(user);

            userRepoService.addUserDataBase(user);

        }

        return telegrambot.onWebhookUpdateReceived(update);
    }

    @GetMapping("/")
    public List<UserBot> requestResponseForUser(){
        return userRepo.findAll();
    }



}
