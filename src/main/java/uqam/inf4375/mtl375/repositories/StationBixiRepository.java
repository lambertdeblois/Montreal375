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
  
  private static final String FIND_BY_ID_STMT = "select id, name, lat, longueur, nbBikes, nbEmptydocks from stationbixi where id=?";
  
  public StationBixi findById(int id) {
      return jdbcTemplate.queryForObject(FIND_BY_ID_STMT, new Object[]{id}, new StationBixiRowMapper());
  }
  
  
  private static final String FIND_BY_RAYON_STMT = "select * from stationbixi r where st_dwithin(r.location, st_makepoint(?, ?), ?) and nbBikes >= ?";
  
  public List<StationBixi> findWithParameters (int rayon, Double lat, Double longueur, int nbBikes){
      return jdbcTemplate.query(conn -> {
          PreparedStatement ps = conn.prepareStatement(FIND_BY_RAYON_STMT);
          ps.setInt(3, rayon);
          ps.setDouble(1, lat);
          ps.setDouble(2, longueur);
          ps.setInt(4, nbBikes);
          return ps;
      }, new StationBixiRowMapper());
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
      ps.setDouble(3, station.getLat());
      ps.setDouble(4, station.getLongueur());
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
      , rs.getDouble("lat")
      , rs.getDouble("longueur")
      , rs.getInt("nbBikes")
      , rs.getInt("nbEmptyDocks")
    );
  }
}
