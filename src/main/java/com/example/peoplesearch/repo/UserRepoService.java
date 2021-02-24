package com.example.peoplesearch.repo;

import com.example.peoplesearch.domain.UserBot;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class UserRepoService {

    private UserRepo userRepo;

    private List<UserBot> userBots;

    private Set<User> userSet;

    public UserRepoService(UserRepo userRepo, List<UserBot> userBots) {
        this.userRepo = userRepo;
        this.userBots = userBots;
    }

    @Cacheable(value = "users",key = "{#user.id, #user.firstName, #user.lastName, #user.userName}")//, #user.firstName, #user.lastName, #user.userName
    public UserBot addUserDataBase(User user){
        System.out.println("Выполнил Cacheable");
        return createUser(user);
    }

    @CachePut(value = "users",key="{#user.id, #user.firstName, #user.lastName, #user.userName}")
    public UserBot updateUserDataBase(User user){
        System.out.println("Выполнил CachePut");
        userBots=Collections.unmodifiableList(userRepo.findAll());//отсутствие volataile у userBots не повлияет на потокобезопасность
        return createUser(user);
    }

    public boolean isNewUser(User user){

        UserBot userBot=userBots.stream().filter(x->x.getId().equals(user.getId())).findFirst().get();

        return userBot!=null && userBot.getFirstName().equals(user.getFirstName())
                && userBot.getLastName().equals(user.getLastName())
                && userBot.getUserName().equals(user.getUserName());

    }

    private UserBot createUser(User user) {
        UserBot userBot=new UserBot();
        userBot.setId(user.getId());
        userBot.setFirstName(user.getFirstName());
        userBot.setLastName(user.getLastName());
        userBot.setUserName(user.getUserName());
        userRepo.save(userBot);
        return userRepo.save(userBot);
    }


//
//    @Cacheable("users")
//    private UserBot getUser(Integer id){
//        return userRepo.findById(id).get();
//    }



}
