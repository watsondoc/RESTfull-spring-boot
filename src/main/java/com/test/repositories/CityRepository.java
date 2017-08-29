package com.test.repositories;

import com.test.entities.City;
import com.test.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public interface CityRepository extends CrudRepository<City, Long> {

    City findByName(String name);
    //Iterable<City> findAllByOrderByIdDesc();

}

/*@Repository
public class CityRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // thanks Java 8, look the custom RowMapper
    public List<City> findAll() {

        List<City> result = jdbcTemplate.query(
                "SELECT id, name FROM public.\"Cities\"",
                (rs, rowNum) -> new City(rs.getInt("id"),
                        rs.getString("name"))
        );
        return result;
    }

    // Add new city
    public int addCustomer(String name) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        return jdbcTemplate.update("INSERT INTO Cities(name,\"createdAt\",\"updatedAt\") VALUES (?)",
                name,timeStamp,timeStamp);
    }

}*/