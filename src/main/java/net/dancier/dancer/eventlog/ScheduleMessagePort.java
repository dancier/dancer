package net.dancier.dancer.eventlog;

public interface ScheduleMessagePort {
    void schedule(Object data, String key, String type);

}
