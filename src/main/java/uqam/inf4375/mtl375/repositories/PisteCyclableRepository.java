package uqam.inf4375.mtl375.repositories;

import uqam.inf4375.mtl375.domain.PisteCyclable;

import java.util.*;
import java.sql.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;

@Component
public class PisteCyclableRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String FIND_ALL_STMT = "select id, geom from pistecyclable";

    /**
     * Returns all pisteCyclables from the DB.
     *
     * @return list of all the pisteCyclables.
     */
    public List<PisteCyclable> findAll() {
        return jdbcTemplate.query(FIND_ALL_STMT, new PisteCyclableRowMapper());
    }

    private static final String INSERT_STMT
            = " insert into pistecyclable (id, geom)"
            + " values (?, ST_GeomFromText(?, 4326))"
            + " on conflict do nothing";

    /**
     * Insert a pisteCyclable into the DB.
     *
     * @param piste the piste to insert.
     * @return
     */
    public int insert(PisteCyclable piste) {
        return jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(INSERT_STMT);
            ps.setInt(1, piste.getId());
            ps.setString(2, piste.getGeom());
            return ps;
        });
    }

    private static final String GET_PISTE_STMT = "select * from pistecyclable where st_distance(geom::geography, st_setsrid(st_makepoint(?, ?), 4326)::geography) < ?";

    /**
     * Find a pisteCyclable by its coordinates.
     *
     * @param lat the geolocalisation
     * @param longueur the geolocalisation
     * @param rayon the geolocalisation
     * @return pisteCyclable
     */
    public List<PisteCyclable> findWithPoint(Double lat, Double longueur, int rayon) {
        return jdbcTemplate.query(conn -> {
            PreparedStatement ps = conn.prepareStatement(GET_PISTE_STMT);
            ps.setDouble(1, longueur);
            ps.setDouble(2, lat);
            ps.setInt(3, rayon);
            return ps;
        }, new PisteCyclableRowMapper());
    }

}

class PisteCyclableRowMapper implements RowMapper<PisteCyclable> {

    /**
     * Returns a pisteCyclable from the DB.
     */
    public PisteCyclable mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PisteCyclable(
                rs.getInt("id"),
                rs.getString("geom")
        );
    }
}
