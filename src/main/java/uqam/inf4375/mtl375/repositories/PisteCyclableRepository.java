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
import org.postgresql.*;

@Component
public class PisteCyclableRepository {

  @Autowired private JdbcTemplate jdbcTemplate;

  private static final String FIND_ALL_STMT = "select id, geom from pistecyclable";

  public List<PisteCyclable> findAll() {
    return jdbcTemplate.query(FIND_ALL_STMT, new PisteCyclableRowMapper());
  }

  private static final String INSERT_STMT =
      " insert into pistecyclable (id, geom)"
    + " values (?, ST_GeomFromText(?, 4326))"
    + " on conflict do nothing"
    ;

  public int insert(PisteCyclable piste) {
    return jdbcTemplate.update(conn -> {
      PreparedStatement ps = conn.prepareStatement(INSERT_STMT);
      ps.setInt(1, piste.getId());
      ps.setString(2, piste.getGeom());
      return ps;
    });
  }

  private static final String GET_PISTE_STMT = "select * from pistecyclable where ST_dwithin(geom, ST_SetSRID(ST_MakePoint(?, ?), 4326), ?)";

  public List<PisteCyclable> findWithPoint(Double lat, Double longueur, int rayon){
      return jdbcTemplate.query(conn -> {
          PreparedStatement ps = conn.prepareStatement(GET_PISTE_STMT);
          ps.setDouble(1, lat);
          ps.setDouble(2, longueur);
          ps.setInt(3, rayon);
          return ps;
      }, new PisteCyclableRowMapper());
  }

}

class PisteCyclableRowMapper implements RowMapper<PisteCyclable> {
  public PisteCyclable mapRow(ResultSet rs, int rowNum) throws SQLException {
    return new PisteCyclable(
        rs.getInt("id")
      , rs.getString("geom")
    );
  }
}
