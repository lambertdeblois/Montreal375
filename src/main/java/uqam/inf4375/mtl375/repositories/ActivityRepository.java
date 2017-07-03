/*
 * Copyright 2017 Vincent Lafrenaye-Lirette <vi.lirette@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uqam.inf4375.mtl375.repositories;

import uqam.inf4375.mtl375.domain.Activity;
import java.util.*;
import java.util.stream.*;
import java.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ActivityRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_STMT
            = " insert into activities (id, name, description, district)"
            + " values (?, ?, ?, ?)"
            + " on conflict do nothing";

    public int insert(Activity activity) {
        return jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(INSERT_STMT);
            ps.setInt(1, activity.getId());
            ps.setString(2, activity.getName());
            ps.setString(3, activity.getDescription());
            ps.setString(4, activity.getDistrict());
            return ps;
        });
    }

    private static final String FIND_ALL_STMT
            = "select"
            + "  id, name, description, district"
            + "from activities"
            + "where id = ?"
            ;
    
    public List<Activity> findAll(){
        return jdbcTemplate.query(FIND_ALL_STMT, new ActivityRowMapper());
    }
}

class ActivityRowMapper implements RowMapper<Activity> {

    @Override
    public Activity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Activity(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getString("district"));
    }
}
