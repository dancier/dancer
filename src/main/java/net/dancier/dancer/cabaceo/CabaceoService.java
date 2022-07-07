package net.dancier.dancer.cabaceo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CabaceoService {

    public static final Logger log = LoggerFactory.getLogger(CabaceoService.class);

    public List<Cabaceo> getCabaceoThread(UUID user, UUID partner) {
        return List.of();
    }

    public List<UUID> getContactPartners(UUID uuid) {
        return List.of();
    }

    public void send(UUID from, UUID to, String message) {
        log.info("Got cabaceo from {} to {} with payload {}.", from, to, message);
    }
}
