package com.example.userservice.shelduer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@EnableScheduling
@Slf4j
@Component
public class Scheduler {

    @Scheduled(fixedDelay = 5000)
    public void fixedDelayTask(){

    }

    @Scheduled(fixedDelay = 2000)
    public void fixedRateTask(){
        log.info("fixedDelayTask ran on" + LocalDateTime.now());
    }
}