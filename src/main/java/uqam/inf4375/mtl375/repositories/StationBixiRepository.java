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

@Component
public class StationBixiRepository {

  @Autowired private JdbcTemplate jdbcTemplate;

  private static final String FIND_ALL_STMT = "select id, name, lat, longueur, nbbikes, nbemptydocks from stationbixi";

  public List<StationBixi> findAll() {
    return jdbcTemplate.query(FIND_ALL_STMT, new StationBixiRowMapper());
  }

  private static final String INSERT_STMT =
      " insert into stationbixi (id, name, lat, longueur, nbBikes, nbEmptyDocks)"
    + " values (?, ?, ?, ?, ?, ?)"
    + " on conflict do nothing"
    ;

  public int insert(StationBixi station) {
    return jdbcTemplate.update(conn -> {
      PreparedStatement ps = conn.prepareStatement(INSERT_STMT);
      ps.setInt(1, station.getId());
      ps.setString(2, station.getName());
      ps.setFloat(3, station.getLat());
      ps.setFloat(4, station.getLongueur());
      ps.setInt(5, station.getNbBikes());
      ps.setInt(6, station.getNbEmptyDocks());
      return ps;
    });
  }
}

class StationBixiRowMapper implements RowMapper<StationBixi> {
  public StationBixi mapRow(ResultSet rs, int rowNum) throws SQLException {
    return new StationBixi(
        rs.getInt("id")
      , rs.getString("name")
      , rs.getFloat("lat")
      , rs.getFloat("longueur")
      , rs.getInt("nbBikes")
      , rs.getInt("nbEmptyDocks")
    );
  }
}
