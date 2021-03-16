package net.dancier.db;

import net.dancier.domain.User;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.UUID;

public interface UserDao {

    @SqlQuery("select * from dancier.public.user;")
    @RegisterRowMapper(UserMapper.class)
    List<User> getAll();

    @SqlUpdate("INSERT INTO dancier.public.user (id, user_name, id_system, foreign_id, email) " +
               "VALUES (:id, :userName, :idSystem, :foreignId, :email)")
    void insertBean(@BindBean User user);
}
