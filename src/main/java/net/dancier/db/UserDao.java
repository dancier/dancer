package net.dancier.db;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.UUID;

public interface UserDao {

    @SqlUpdate("INSERT INTO dancier.public.user (id, id_system, foreign_id, user_name)" +
            " VALUES (:id, :id_system, :foreign_id, :user_name)")
    void insert(@Bind("id") UUID id,
                @Bind("id_system") String idSystem,
                @Bind("foreign_id") String foreignId,
                @Bind("user_name") String userName);
}
