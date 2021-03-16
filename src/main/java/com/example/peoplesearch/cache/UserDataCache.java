package com.example.peoplesearch.cache;


import com.example.peoplesearch.botapi.BotState;
import com.example.peoplesearch.botapi.handlers.fillingprofile.UserProfileData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserDataCache implements DataCache {

    private Map<Integer, BotState> usersBotStates = new ConcurrentHashMap<>();//храним ID и состояние пользователя
    private Map<Integer, UserProfileData> usersProfileData = new ConcurrentHashMap<>();//храним ID и данные пользователя
    private Map<Integer, Map<String,UserProfileData>> userProfilesData = new ConcurrentHashMap<>();
    //храение профилей users во время обработки запроса по id кнопок
    @Override
    public void setUsersCurrentBotState(int userId, BotState botState) {

        usersBotStates.put(userId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(int userId) {//Если текущее состояние не найдено,
        // то ставим пользователя в начальное сотояние
        BotState botState=usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.INITIAL_MESSAGE;
        }

        return botState;
    }

    @Override
    public UserProfileData getUserProfileData(int userId) {

        UserProfileData userProfileData=this.usersProfileData.get(userId);
        if (userProfileData == null) {
            userProfileData = new UserProfileData();
        }
        return userProfileData;
    }

    @Override
    public void saveUserProfileData(int userId, UserProfileData userProfileData) {

        usersProfileData.put(userId, userProfileData);
    }

    public void removeUserProfileData(int userId) {

        usersProfileData.remove(userId);
    }

    public void setUserProfilesData(int userId, String callBackData, UserProfileData userProfileData) {

        userProfilesData.get(userId).put(callBackData,userProfileData);
    }

    public UserProfileData getUserProfilesData(int userId, String callBackData){

        if(userProfilesData.get(userId)==null){
            userProfilesData.put(userId,new HashMap<>());
        }
        return userProfilesData.get(userId).get(callBackData);
    }


    public void removeUserProfilesData(int userId, String callBackData) {
        userProfilesData.get(userId).remove(callBackData);
    }
}
