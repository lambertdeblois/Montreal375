package uqam.inf4375.mtl375.repositories;

import uqam.inf4375.mtl375.domain.*;

import java.sql.Array;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgresql.util.PGobject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.dao.EmptyResultDataAccessException;

@Component
public class ActivityRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_STMT
            = " insert into activities (id, name, description, district, dates, nomPlace, lieu)"
            + " values (?, ?, ?, ?, ?, ?, ST_SetSRID(ST_MakePoint(?, ?), 4326))"
            + " on conflict do nothing";

    public int insert(Activity activity) {
        return jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(INSERT_STMT);
            ps.setInt(1, activity.getId());
            ps.setString(2, activity.getName());
            ps.setString(3, activity.getDescription());
            ps.setString(4, activity.getDistrict());
            Array dates = conn.createArrayOf("DATE", activity.getDates());
            ps.setArray(5, dates);
            ps.setString(6, activity.getPlace().getName());
            ps.setDouble(7, 0.0);
            if (activity.getPlace().getLatitude() != null){
              ps.setDouble(7, activity.getPlace().getLatitude());
            }
            ps.setDouble(8, 0.0);
            if (activity.getPlace().getLongitude() != null){
              ps.setDouble(8, activity.getPlace().getLongitude());
            }
            return ps;
        });
    }

    private static final String FIND_ID_STMT = "select * from activities where id=?";

 public Activity findById(int id) {
   try{
     return jdbcTemplate.queryForObject(FIND_ID_STMT, new Object[]{id}, new ActivityRowMapper());
   } catch (EmptyResultDataAccessException e) {
     return null;
   }
}

 private static final String FIND_BY_CONTENU_WITH_DATES_STMT =
      " select"
    + "     id"
    + "   , ts_headline(name, q, 'HighlightAll = true') as name"
    + "   , description"
    + "   , district"
    + "   , dates"
    + "   , nomPlace"
    + "   , lieu"
    + " from"
    + "     activities"
    + "   , to_tsquery(?) as q"
    + " where"
    + "   name @@ q and ? <= all(dates) and ? >= all(dates)"
    + " order by"
    + "   ts_rank_cd(to_tsvector(name), q) desc"
    ;

  public List<Activity> findByContenuWithDates(Date from, Date to, String... tsterms) {
    String tsquery = Arrays.stream(tsterms).collect(Collectors.joining(" & "));
    return jdbcTemplate.query(conn -> {
        PreparedStatement ps = conn.prepareStatement(FIND_BY_CONTENU_WITH_DATES_STMT);
        ps.setObject(1, tsquery);
        ps.setDate(2, from);
        ps.setDate(3, to);
        return ps;
    }, new ActivityRowMapper());
  }


 private static final String FIND_BY_CONTENU_STMT =
      " select"
    + "     id"
    + "   , ts_headline(name, q, 'HighlightAll = true') as name"
    + "   , description"
    + "   , district"
    + "   , dates"
    + "   , nomPlace"
    + "   , lieu"
    + " from"
    + "     activities"
    + "   , to_tsquery(?) as q"
    + " where"
    + "   name @@ q"
    + " order by"
    + "   ts_rank_cd(to_tsvector(name), q) desc"
    ;

  public List<Activity> findByContenu(String... tsterms) {
    String tsquery = Arrays.stream(tsterms).collect(Collectors.joining(" & "));
    return jdbcTemplate.query(FIND_BY_CONTENU_STMT, new Object[]{tsquery}, new ActivityRowMapper());
  }

private static final String DELETE_ID_STMT = "delete from activities where id=?";

    public int delete(int id) {
        return jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(DELETE_ID_STMT);
            ps.setInt(1,id);
            return ps;
        });
}

    private static final String FIND_ALL_STMT = "select * from activities";

    public List<Activity> findAll(){
        return jdbcTemplate.query(FIND_ALL_STMT, new ActivityRowMapper());
    }

    private static final String FIND_COORD_DATES_STMT = "select * from activities where ? <= all(dates) and ? >= all(dates) and st_dwithin(lieu, st_makepoint(?, ?), ?)";

    public List<Activity> findWithCoordDates(Date from, Date to, Double lat, Double longueur, int rayon){
        return jdbcTemplate.query(conn -> {
            PreparedStatement ps = conn.prepareStatement(FIND_COORD_DATES_STMT);
            ps.setDate(1, from);
            ps.setDate(2, to);
            ps.setDouble(3, lat);
            ps.setDouble(4, longueur);
            ps.setInt(5, rayon);
            return ps;
        }, new ActivityRowMapper());
    }

    private static final String FIND_DATES_STMT = "select * from activities where ? <= all(dates) and ? >= all(dates)";

    public List<Activity> findWithDates(Date from, Date to) {
        return jdbcTemplate.query(conn -> {
            PreparedStatement ps = conn.prepareStatement(FIND_DATES_STMT);
            ps.setDate(1, from);
            ps.setDate(2, to);
            return ps;
        }, new ActivityRowMapper());
    }

    private static final String FIND_COORD_STMT = "select * from activities where st_dwithin(lieu, st_makepoint(?, ?), ?)";

    public List<Activity> findWithCoord(Double lat, Double longueur, int rayon){
        return jdbcTemplate.query(conn -> {
            PreparedStatement ps = conn.prepareStatement(FIND_COORD_STMT);
            ps.setDouble(1, lat);
            ps.setDouble(2, longueur);
            ps.setInt(3, rayon);
            return ps;
        }, new ActivityRowMapper());
    }
}

class ActivityRowMapper implements RowMapper<Activity> {

    @Override
    public Activity mapRow(ResultSet rs, int rowNum) throws SQLException {
        Array dates = rs.getArray("dates");
        Date[] lDates = (Date[])dates.getArray();

        PGobject pg = (PGobject) rs.getObject("lieu");
        Point pt = (Point) PGgeometry.geomFromString(pg.getValue());
        String nomPlace = rs.getString("nomPlace");
        Place place = new Place(nomPlace, pt.x, pt.y);

        return new Activity(
        rs.getInt("id"),
        rs.getString("name"),
        rs.getString("description"),
        rs.getString("district"),
        lDates,
        place
        );
    }
}
