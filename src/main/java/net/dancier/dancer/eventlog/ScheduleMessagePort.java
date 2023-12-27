package net.dancier.dancer.eventlog;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cloudevents.CloudEvent;

import java.net.URI;

public interface ScheduleMessagePort {
    void schedule(Object object,
                  String key,
                  URI source,
                  String type) throws JsonProcessingException;

}
