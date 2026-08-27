package gestioepis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("desktop")
public class DesktopShutdownController {

    @Autowired
    private ApplicationContext applicationContext;

    @PostMapping("/desktop/shutdown")
    public void shutdown() {
        new Thread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
            }
            SpringApplication.exit(applicationContext, () -> 0);
            System.exit(0);
        }).start();
    }
}
