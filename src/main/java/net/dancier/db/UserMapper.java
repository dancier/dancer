package net.dancier.db;

import net.dancier.domain.User;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class UserMapper implements RowMapper<User> {

    public static Logger logger = LoggerFactory.getLogger(UserMapper.class);

    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
        User user = new User();
        user.setId(UUID.fromString(rs.getString("id")));
        user.setForeignId(rs.getString("foreign_id"));
        user.setIdProvider(User.IdProvider.valueOf(rs.getString("id_provider")));
        user.setEmail(rs.getString("email"));
        return user;
    }
}
