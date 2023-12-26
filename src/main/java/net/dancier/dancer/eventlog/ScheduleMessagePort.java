package net.dancier.dancer.eventlog;

import io.cloudevents.CloudEvent;

public interface ScheduleMessagePort {
    void schedule(CloudEvent cloudEvent, String key);

}
