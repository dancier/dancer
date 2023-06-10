package net.dancier.dancer.output;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class OutboxJob {
    private final Logger log = LoggerFactory.getLogger(OutboxJob.class);

    @Scheduled(fixedRate = 10000)
    public void process() {
        log.debug("Sending out events");
    }
}
