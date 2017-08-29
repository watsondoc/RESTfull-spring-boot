package com.test.specifications;

import com.test.entities.Person;
import org.apache.catalina.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.ArrayList;
import java.util.List;

public class PersonSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public PersonSpecificationsBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public PersonSpecificationsBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Person> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<Person>> specs = new ArrayList<Specification<Person>>();
        for (SearchCriteria param : params) {
            specs.add(new PersonSpecification(param));
        }

        Specification<Person> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        return result;
    }
}
