package net.dancier.db;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;
import java.util.UUID;

public interface DancerDao {

    @SqlQuery("""
            SELECT * 
              FROM dancer
             WHERE user_id = :id
            """)
    @RegisterRowMapper(DancerDtoMapper.class)
    Optional<DancerDto> getById(@Bind("id") UUID user_id);

    @SqlUpdate("""
            INSERT 
              INTO dancer (user_id, user_name, about_him)
              VALUES (:userId, :userName, :aboutHim)
         ON CONFLICT (user_id)
            DO UPDATE SET
              user_name = :userName,
              size = :size,
              about_him = :aboutHim,
              birth_date = :birthDate,
              smoker = :smoker,
              image_id = :imageId   
            WHERE dancer.user_id = :userId    
            """)
        void createOrUpdate(@BindBean DancerDto dancer);
}
