package gestioepis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("desktop")
public class DesktopBrowserLauncher {

    private static final Logger log = LoggerFactory.getLogger(DesktopBrowserLauncher.class);

    @Value("${server.port}")
    private int serverPort;

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        String url = "http://localhost:" + serverPort;
        log.info("Obrint el navegador a {}", url);
        try {
            new ProcessBuilder("cmd", "/c", "start", "", url).start();
        } catch (Exception e) {
            log.warn("No s'ha pogut obrir el navegador automaticament", e);
        }
    }
}
