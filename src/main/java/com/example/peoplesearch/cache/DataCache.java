package com.example.peoplesearch.cache;

import com.example.peoplesearch.botapi.BotState;
import com.example.peoplesearch.botapi.handlers.fillingprofile.UserProfileData;

public interface DataCache {
    void setUsersCurrentBotState(int userId, BotState botState);//назначить для пользователя состояние

    BotState getUsersCurrentBotState(int userId);//Получить состояние пользователя

    UserProfileData getUserProfileData(int userId);

    void saveUserProfileData(int userId, UserProfileData userProfileData);
}
