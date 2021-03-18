package net.dancier.db;

import net.dancier.domain.User;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {

    @SqlQuery("""
            SELECT * 
              FROM "user"
            """)
    @RegisterRowMapper(UserMapper.class)
    List<User> getAll();

    @SqlQuery("""
            SELECT * 
              FROM "user"
             WHERE id_provider = :idProvider 
               AND foreign_id = :foreignId
              """)
    @RegisterRowMapper(UserMapper.class)
    Optional<User> lookUpByIdProviderAndForeignId(@Bind("idProvider") User.IdProvider idProvider, @Bind("foreignId") String foreignId);

    @SqlQuery("""
            SELECT * 
              FROM "user"
             WHERE id = :id
              """)
    @RegisterRowMapper(UserMapper.class)
    Optional<User> lookUpById(@Bind("id") UUID userId);

    @SqlUpdate("""
                INSERT INTO "user" 
                (id, id_provider, foreign_id, email)
                VALUES (:id, :idProvider, :foreignId, :email)
                """)
    void insertUser(@BindBean User user);


}
