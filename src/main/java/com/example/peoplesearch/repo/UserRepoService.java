package com.example.peoplesearch.repo;

import com.example.peoplesearch.domain.UserBot;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserRepoService {

    private UserRepo userRepo;
    private List<UserBot> userBots;

    public UserRepoService(UserRepo userRepo, List<UserBot> userBots) {
        this.userRepo = userRepo;
        this.userBots = userBots;
    }

    @Cacheable(value = "users",key = "{#user.id, #user.firstName, #user.lastName, #user.userName}")//, #user.firstName, #user.lastName, #user.userName
    public UserBot addUserDataBase(User user){
        System.out.println("Выполнил Cacheable");
        return userRepo.save(createUserBot(user));
    }

    @CachePut(value = "users",key="{#user.id, #user.firstName, #user.lastName, #user.userName}")
    public UserBot updateUserDataBase(User user){
        System.out.println("Выполнил CachePut");
        UserBot userBot=userRepo.save(createUserBot(user));
        userBots=Collections.unmodifiableList(userRepo.findAll());//отсутствие volataile у userBots не повлияет на потокобезопасность
        return userBot;
    }

    public boolean isNewUser(User user){

        Optional<UserBot> opt=userBots.stream().filter(x->x.getId().equals(user.getId())).findFirst();

        return opt.isPresent() && opt.get().equals(createUserBot(user));

    }

    private UserBot createUserBot(User user) {
        UserBot userBot=new UserBot();
        userBot.setId(user.getId());
        userBot.setFirstName(user.getFirstName());
        userBot.setLastName(user.getLastName());
        userBot.setUserName(user.getUserName());
        return userBot;
    }


//
//    @Cacheable("users")
//    private UserBot getUser(Integer id){
//        return userRepo.findById(id).get();
//    }



}
