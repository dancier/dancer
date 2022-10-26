package net.dancier.dancer.eventlog.service;

import net.dancier.dancer.eventlog.model.Eventlog;

public interface EventlogS3Service {
    void storeEventLog(Eventlog entry);

}
