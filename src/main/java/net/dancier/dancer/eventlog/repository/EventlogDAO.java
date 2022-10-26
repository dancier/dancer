package net.dancier.dancer.eventlog.repository;

import net.dancier.dancer.eventlog.model.Eventlog;

import java.sql.SQLException;
import java.util.List;

public interface EventlogDAO {
    void update(Eventlog eventlog) throws SQLException;
    List<Eventlog> lockAndGet(Integer size);

    void schedule(Eventlog eventlog) throws SQLException;
    int getCountOfEventlogEntries();

}
