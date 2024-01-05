package com.nicode.users.usersapi.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DatabaseKeepAlive {
    @Scheduled(fixedRate = 900000)
    public void keepDatabaseConnectionAlive() {
        System.out.println("ping api............");
    }
}
