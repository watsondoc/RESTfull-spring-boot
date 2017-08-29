package com.test.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.entities.Attribute;
import com.test.entities.City;
import com.test.entities.Person;
import com.test.entities.Value;
import com.test.jsonentities.JsonInPerson;
import com.test.jsonentities.JsonOutPerson;
import com.test.repositories.AttributeRepository;
import com.test.repositories.CityRepository;
import com.test.repositories.PersonRepository;
import com.test.repositories.ValueRepository;
import com.test.services.PersonService;
import com.test.specifications.PersonSpecificationsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonServiceImpl implements PersonService{

    @Autowired
    private PersonRepository personRepository;
    @Autowired // This means to get the bean called cityRepository
    private CityRepository cityRepository;
    @Autowired // This means to get the bean called cityRepository
    private AttributeRepository attrRepository;
    @Autowired // This means to get the bean called cityRepository
    private ValueRepository valueRepository;

    @Override
    public List<JsonOutPerson> getAllPersons(Integer page,Integer page_entries,String sortStr,String filters) throws IOException {

        Sort sort = this.generateSorting(sortStr);
        Specification<Person> spec = this.generateQuerySpecification(filters);

        if(page == null || page<=0) {
            page = 1;
        }
        if(page_entries == null || page_entries<0) {
            page_entries = 100;
        }

        Pageable pageable = new PageRequest(page-1, page_entries,sort);

        Iterable<Person> mPersons =  personRepository.findAll(spec,pageable);
        List<JsonOutPerson> mOutPersons = new ArrayList<JsonOutPerson>();
        for(Person prs: mPersons){
            mOutPersons.add(new JsonOutPerson(prs));
        }
        return mOutPersons;
    }

    /**
     * Generate Sort object from request sort params
     * @param sortStr request sort string param
     * @return Sort object or null if sortStr is empty or null
     */
    public Sort generateSorting(String sortStr){
        Sort sort = null;
        String[] parts = new String[0];
        if(sortStr!=null) {
            parts = sortStr.split(",");
        }

        for (String item:parts) {
            if(sort == null){
                String[] sortType = item.split("-");
                Sort.Direction direction;
                String prop;
                if(sortType.length>1) {
                    prop = sortType[1];
                    direction = Sort.Direction.DESC;
                }
                else {
                    prop = sortType[0];
                    direction = Sort.Direction.ASC;
                }
                sort = new Sort(direction,prop);
            }
            else{

            }
        }
        return sort;
    }

    /**
     * Generate JPA Specification object by giving json filter string
     * @param filters request filters string param
     * @return Specification<Person> object or null if filters is empty or null
     * @throws IOException
     */
    Specification<Person> generateQuerySpecification(String filters) throws IOException {
        Specification<Person> spec = null;
        if(filters!=null) {
            Map<String,String> map = new HashMap<String,String>();
            ObjectMapper mapper = new ObjectMapper();
            map = mapper.readValue(filters, HashMap.class);
            PersonSpecificationsBuilder builder = new PersonSpecificationsBuilder();
            for(String key:map.keySet()){
                if(key.equals("city")){
                    builder.with("name", "cityFilter", map.get(key));
                }
            }
            spec = builder.build();
        }
        return spec;
    }

    @Override
    public Long getPersonsCount() {
        return personRepository.count();
    }

    @Override
    public JsonOutPerson findById(Long id) {
        Person person = personRepository.findOne(id);
        return new JsonOutPerson(person);
    }

    @Override
    public void deletePersonById(Long id) {
        Person deletedPrs = personRepository.findOne(id);
        if(deletedPrs!=null) {
            valueRepository.deleteAllByPerson(deletedPrs);
            personRepository.delete(deletedPrs);
        }
    }

    @Override
    public JsonOutPerson createPerson(JsonInPerson person) {
        Person personToSave = person.getDefaultPerson();
        this.setCityByName(personToSave,person.getCity());
        Person mperson = personRepository.save(personToSave);
        return new JsonOutPerson(mperson);
    }

    @Override
    public JsonOutPerson updatePerson(Long id, JsonOutPerson person) {
        Person personToSave = personRepository.findOne(id);
        person.setDefaultFields(personToSave);
        this.setCityByName(personToSave,person.getCity());
        personRepository.save(personToSave);
        this.checkAndUpdateValues(id,person);
        return new JsonOutPerson(personToSave);
    }

    /**
     * Find City by name and set to Person entity City field
     * @param personToSave instans of person
     * @param name City name to be find
     */
    public void setCityByName(Person personToSave, String name){
        City city = cityRepository.findByName(name);
        personToSave.setCity(city);
    }


    private void checkAndUpdateValues(Long id, JsonOutPerson person){
        Person pers = personRepository.findOne(id);
        HashMap<String,Object> currentValues= person.parseValues(pers.getValues());
        ArrayList<String> itemsToDelete = new ArrayList<String>();
        ArrayList<String> itemsToUpdate = new ArrayList<String>();
        ArrayList<String> itemsToAdd = new ArrayList<String>();

        for(String val:currentValues.keySet()){
            if(person.getAttributes().containsKey(val)){
                itemsToUpdate.add(val);
            }
            else{
                itemsToDelete.add(val);
            }
        }

        if(person.getAttributes()!=null) {
            for (String val : person.getAttributes().keySet()) {
                if (!currentValues.containsKey(val)) {
                    itemsToAdd.add(val);
                }
            }
        }

        Iterable<Attribute> attrDelList = attrRepository.findAllByNameIn(itemsToDelete);
        for(Attribute attr:attrDelList){
            /*Iterable<Value> relatedValues = valueRepository.findAllByAttrAndPerson(attr,pers);
            for(Value val:relatedValues){
                valueRepository.delete(val);
            }*/
            valueRepository.deleteAllByAttrAndPerson(attr,pers);
            //valueRepository.deleteAllByAttr(attr);
        }

        //Iterable<Attribute> attrAddList = attrRepository.findAllByNameIn(itemsToAdd);
        for(String item:itemsToAdd){
            Attribute attr = attrRepository.findByName(item);
            Value val = null;

            if(!attr.getType().equals("array")){
                val = new Value(attr,pers);
                val.setValue(person.getAttributes().get(item).toString());
                valueRepository.save(val);
            }
            else{
                ArrayList<String> dataItems = (ArrayList<String>)person.getAttributes().get(item);
                for(String dataItem:dataItems){
                    val = new Value(attr,pers);
                    val.setValue(dataItem);
                    valueRepository.save(val);
                }
            }
        }

        for(String item:itemsToUpdate){
            Attribute attr = attrRepository.findByName(item);
            if(!attr.getType().equals("array")){
                Value val = valueRepository.findByAttrAndPerson(attr,pers);
                val.setValue(person.getAttributes().get(item).toString());
                valueRepository.save(val);
            } else{
                ArrayList<String> tosaveItems = (ArrayList<String>)person.getAttributes().get(item);
                ArrayList<String> currentItems = (ArrayList<String>)(new JsonOutPerson(pers)).getAttributes().get(item);

                for(String dataItem:tosaveItems){
                    if(!currentItems.contains(dataItem)){
                        Value vl = new Value(attr,pers);
                        vl.setValue(dataItem);
                        valueRepository.save(vl);
                    }
                }

                for(String dataItem:currentItems){
                    if(!tosaveItems.contains(dataItem)){
                         valueRepository.deleteByAttrAndPersonAndValue(attr,pers,dataItem);
                    }
                }
            }
        }
    }
}
