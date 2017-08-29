package com.test.repositories;

import com.test.entities.Attribute;
import com.test.entities.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttributeRepository extends CrudRepository<Attribute, Long> {

    Iterable<Attribute> findAllByOrderByIdDesc();
    Iterable<Attribute> findAllByNameIn(List<String> names);
    Attribute findByName(String name);

}