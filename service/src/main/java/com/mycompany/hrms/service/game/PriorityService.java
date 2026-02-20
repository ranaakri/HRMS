package com.mycompany.hrms.service.game;

import com.mycompany.hrms.data.entity.game.UserGameStats;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class PriorityService {

    public int calculatePriority(UserGameStats stats){
        int priority = stats.getPriorityScore();

        if(stats.getLastPlayedAt() == null){
            priority += 100;
        }

        long days = 0;
        if(stats.getLastPlayedAt() != null) {
            days = ChronoUnit.DAYS.between(stats.getLastPlayedAt(), ZonedDateTime.now());

            priority += (10 * (int) days);

            if (stats.getLastPlayedAt().equals(ZonedDateTime.now())) {
                priority -= 40;
            }
        }

        return priority;
    }
}
