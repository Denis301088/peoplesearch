package com.example.peoplesearch.repo;

import com.example.peoplesearch.domain.UserBot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserBot,Integer> {

}
