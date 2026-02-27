package com.mycompany.hrms.api.config.startup_service;

import com.mycompany.hrms.service.game.GameSchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupServiceRunner implements CommandLineRunner {

    private final GameSchedulingService myService;

    @Autowired
    public StartupServiceRunner(GameSchedulingService myService) {
        this.myService = myService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Application started. Executing service logic...");
        myService.generateSlots();
    }
}
