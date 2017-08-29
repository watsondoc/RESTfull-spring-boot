package com.test.specifications;


import com.test.entities.City;
import com.test.entities.Person;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class PersonSpecification implements Specification<Person> {
    private SearchCriteria criteria;

    public PersonSpecification(SearchCriteria criteria){
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Join<Person,City> personCity = root.join("city");

        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        else if (criteria.getOperation().equalsIgnoreCase("cityFilter")){
            return builder.like(builder.lower(personCity.get(criteria.getKey())),"%" + criteria.getValue().toString().toLowerCase() + "%");
        }
        
        return null;
    }
}

