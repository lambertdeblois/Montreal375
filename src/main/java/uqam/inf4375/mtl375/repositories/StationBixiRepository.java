package uqam.inf4375.mtl375.repositories;

import uqam.inf4375.mtl375.domain.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;

import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgresql.util.PGobject;

import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;

@Component
public class StationBixiRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String FIND_ALL_STMT = "select id, name, nbBikes, nbEmptyDocks, lieu from stationbixi";

    public List<StationBixi> findAll() {
        return jdbcTemplate.query(FIND_ALL_STMT, new StationBixiRowMapper());
    }

    private static final String FIND_BY_ID_STMT = "select id, name, nbBikes, nbEmptydocks, lieu from stationbixi where id=?";

    public StationBixi findById(int id) {
        return jdbcTemplate.queryForObject(FIND_BY_ID_STMT, new Object[]{id}, new StationBixiRowMapper());
    }

    private static final String FIND_BY_RAYON_STMT = "select * from stationbixi where st_dwithin(lieu, st_makepoint(?, ?), ?) and nbBikes >= ?";

    public List<StationBixi> findWithParameters(int rayon, Double lat, Double longueur, int nbBikes) {
        return jdbcTemplate.query(conn -> {
            PreparedStatement ps = conn.prepareStatement(FIND_BY_RAYON_STMT);
            ps.setDouble(1, lat);
            ps.setDouble(2, longueur);
            ps.setInt(3, rayon);
            ps.setInt(4, nbBikes);
            return ps;
        }, new StationBixiRowMapper());
    }

    private static final String INSERT_STMT
            = " insert into stationbixi (id, name, nbBikes, nbEmptyDocks, lieu)"
            + " values (?, ?, ?, ?, ST_SetSRID(ST_Makepoint(?, ?), 4326))"
            + " on conflict do nothing";

    public int insert(StationBixi station) {
        return jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(INSERT_STMT);
            ps.setInt(1, station.getId());
            ps.setString(2, station.getName());
            ps.setInt(3, station.getNbBikes());
            ps.setInt(4, station.getNbEmptyDocks());
            ps.setDouble(5, station.getLat());
            ps.setDouble(6, station.getLongueur());
            return ps;
        });
    }
}

class StationBixiRowMapper implements RowMapper<StationBixi> {

    public StationBixi mapRow(ResultSet rs, int rowNum) throws SQLException {
        PGobject pg = (PGobject) rs.getObject("lieu");
        Point pt = (Point) PGgeometry.geomFromString(pg.getValue());
        return new StationBixi(
                rs.getInt("id"),
                 rs.getString("name"),
                 rs.getInt("nbBikes"),
                 rs.getInt("nbEmptyDocks"),
                 pt.x,
                 pt.y
        );
    }
}
