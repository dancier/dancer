package net.dancier.db;

import net.dancier.domain.dance.SmokingBehaviour;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class DancerDtoMapper implements RowMapper<DancerDto> {
    @Override
    public DancerDto map(ResultSet rs, StatementContext ctx) throws SQLException {
        DancerDto dancerDto = new DancerDto();
        dancerDto.setUserId(UUID.fromString(rs.getString("user_id")));
        dancerDto.setUserName(rs.getString("user_name"));
        String possibleImageId = rs.getString("image_id");
        if (possibleImageId!=null) {
            dancerDto.setImageId(UUID.fromString(rs.getString("image_id")));
        }
        dancerDto.setSize(rs.getInt("size"));
        dancerDto.setBirthDate(rs.getDate("birth_date"));
        String possibleSmokerString = rs.getString("smoker");
        if (possibleSmokerString!=null) {
            dancerDto.setSmoker(SmokingBehaviour.valueOf(possibleSmokerString));
        }
        dancerDto.setAboutHim(rs.getString("about_him"));
        return dancerDto;
    }
}
