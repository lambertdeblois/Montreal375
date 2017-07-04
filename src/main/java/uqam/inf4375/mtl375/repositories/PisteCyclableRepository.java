package uqam.inf4375.mtl375.repositories;

import java.util.*;
import java.util.stream.*;
import java.sql.*;

import uqam.inf4375.mtl375.domain.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.*;
import org.postgis.*;

@Component
public class PisteCyclableRepository {

  @Autowired private JdbcTemplate jdbcTemplate;

  private static final String FIND_ALL_STMT = "select id, location from pistecyclable";

  public List<PisteCyclable> findAll() {
    return jdbcTemplate.query(FIND_ALL_STMT, new PisteCyclableRowMapper());
  }

  private static final String INSERT_STMT =
      " insert into pistecyclable (id, location)"
    + " values (?, ?)"
    + " on conflict do nothing"
    ;

  public int insert(PisteCyclable piste) {
    return jdbcTemplate.update(conn -> {
      PreparedStatement ps = conn.prepareStatement(INSERT_STMT);
      ps.setDouble(1, piste.getId());
      (PGgeometry)ps.setObject(2, piste.getGeometry());
      return ps;
    });
  }
}

class PisteCyclableRowMapper implements RowMapper<PisteCyclable> {
  public PisteCyclable mapRow(ResultSet rs, int rowNum) throws SQLException {
    return new PisteCyclable(
        rs.getDouble("id")
      , (PGgeometry)rs.getObject("location")
    );
  }
}
