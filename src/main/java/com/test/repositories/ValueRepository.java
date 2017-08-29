package com.test.repositories;

import com.test.entities.Attribute;
import com.test.entities.Person;
import com.test.entities.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ValueRepository extends CrudRepository<Value, Long> {

    @Modifying
    @Query(value = "DELETE FROM 'Value' WHERE 'personId' = :personid and 'attrId' = :attrid", nativeQuery = true)
    void deleteAllValueByAttrIdAndPersonId(@Param("personid") Long personid, @Param("attrid") Long attrid);

    @Query(value = "Insert into \"Value\" (\"attrId\",\"personId\",value) VALUES(:attrId,:personId,:value)", nativeQuery = true)
    void addValueByAttrIdAndPersonId(@Param("personId") Long personId, @Param("attrId") Long attrId, @Param("value") String value);


    //void deleteByAttr(Attribute attr);
    @Modifying
    @Transactional
    void deleteAllByAttr(Attribute attr);

    @Modifying
    @Transactional
    void deleteAllByPerson(Person person);

    @Modifying
    @Transactional
    void deleteAllByAttrAndPerson(Attribute attr,Person person);

    @Modifying
    @Transactional
    void deleteByAttrAndPersonAndValue(Attribute attr,Person person,String value);

    void removeAllByAttrAndPerson(Attribute attr,Person person);

    Long countByAttr(Attribute attribute);

    Iterable<Value> findAllByAttrAndPerson(Attribute attr,Person person);

    Value findByAttrAndPerson(Attribute attr,Person person);

    Long countByAttrAndValue(Attribute attribute, String value);

    Iterable<Value> findAllByAttrAndValueIn(Attribute attribute, List<String> values);

}