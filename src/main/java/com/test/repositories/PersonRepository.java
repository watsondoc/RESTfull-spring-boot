package com.test.repositories;


import com.test.entities.City;
import com.test.entities.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PersonRepository extends CrudRepository<Person,Long>, JpaSpecificationExecutor<Person> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE \"Persons\" SET \"cityId\" = null WHERE \"cityId\" = :id ", nativeQuery = true)
    void deleteCities(@Param("id") Long id);

    Iterable<Person> findAllByOrderByIdAsc();
    Iterable<Person> findAll(Sort var2);
    Iterable<Person> findAll(Pageable var1);

    Long countByCity(City city);


    //Iterable<Person> findByCity

}
    //public Iterable<Person> listOfPersons(@Param("filter") String filterBy, Pageable pageable);
