package net.dancier.db;

import net.dancier.domain.dance.Dancer;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;


public class DancerMapper implements RowMapper<Dancer> {
    @Override
    public Dancer map(ResultSet rs, StatementContext ctx) throws SQLException {
        Dancer dancer = new Dancer();
        dancer.setAboutHim(rs.getString("about_him"));
        dancer.setBirth(rs.getDate("birth_date"));
        dancer.setImage(null);
        dancer.setUserName(rs.getString("user_name"));
        return dancer;
    }
}
