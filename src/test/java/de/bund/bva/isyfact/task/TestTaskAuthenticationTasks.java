package de.bund.bva.isyfact.task;

import static de.bund.bva.isyfact.util.logging.CombinedMarkerFactory.KATEGORIE_JOURNAL;
import static de.bund.bva.isyfact.util.logging.CombinedMarkerFactory.createKategorieMarker;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

@Component
public class TestTaskAuthenticationTasks {

    public static final String SCHLUESSEL = "ISYTA99999";

    private static final Logger LOG = LoggerFactory.getLogger(TestTaskAuthenticationTasks.class);

    public static final AtomicBoolean wasCalled = new AtomicBoolean(false);

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    @Secured("PRIV_Recht1")
    public void scheduledTaskSecured() {
        wasCalled.set(true);
        LOG.info(createKategorieMarker(KATEGORIE_JOURNAL), SCHLUESSEL, "test task - scheduled - secured - executed at {}", LocalDateTime.now());
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    @Secured("PRIV_Recht1")
    public void scheduledTaskSecuredInsufficientRights() {
        LOG.info(createKategorieMarker(KATEGORIE_JOURNAL), SCHLUESSEL, "test task - scheduled - insufficient rights - executed at {}", LocalDateTime.now());
    }

}
