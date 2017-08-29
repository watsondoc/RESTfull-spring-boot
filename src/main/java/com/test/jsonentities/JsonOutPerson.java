package com.test.jsonentities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.entities.Attribute;
import com.test.entities.City;
import com.test.entities.Person;
import com.test.entities.Value;
import com.test.repositories.AttributeRepository;
import com.test.repositories.CityRepository;
import com.test.repositories.PersonRepository;
import com.test.repositories.ValueRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class JsonOutPerson {

    public JsonOutPerson(Person person){
        this.attributes = new HashMap<String,Object>();
        this.id = person.getId();
        this.name = person.getName();
        this.address = person.getAddress();
        this.birthday = person.getBirthday();
        this.age = person.getAge();
        if(person.getCity()!=null) {
            this.city = person.getCity().getName();
        }
        if(person.getValues()!=null && person.getValues().size()>0){
            this.attributes = this.parseValues(person.getValues());
        }
    }

    protected JsonOutPerson(){

    }

    private Long id;
    private String name;
    private String address;
    private Integer age;
    private Date birthday;
    private Person updatedPerson;
    @JsonProperty("City")
    private String city;


    @JsonProperty("Value")
    private HashMap<String,Object> attributes;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public HashMap<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Get List of Values Entities and generate Values like json HashMap
     * @param values List of Value entities
     * @return HashMap<String,Object> object like json
     */
    public HashMap<String,Object> parseValues(List<Value> values){
        HashMap<String,Object> mattributes = new HashMap<String,Object>();
        for(Value vl: values){
            Attribute attr = vl.getAttr();
            if(attr.getType().equals("array")){
                if(mattributes.containsKey(attr.getName())){
                    ((ArrayList<String>)mattributes.get(attr.getName())).add(vl.getValue());
                }
                else{
                    ArrayList<String> attrData = new ArrayList<String>();
                    attrData.add(vl.getValue());
                    mattributes.put(attr.getName(),attrData);
                }
            }
            else{
                mattributes.put(attr.getName(),vl.getValue());
            }
        }
        return mattributes;
    }

    @JsonIgnore
    public void setDefaultFields(Person updatedPerson){
        updatedPerson.setId(this.getId());
        updatedPerson.setName(this.getName());
        updatedPerson.setAddress(this.getAddress());
        updatedPerson.setAge(this.getAge());
        //updatedPerson.setBirthday(this.getBirthday());
    }

    private void deleteAttribute(AttributeRepository attrRepository,List<String> attrName){
        Iterable<Attribute> attrToDelete = attrRepository.findAllByNameIn(attrName);
        for(Attribute attr:attrToDelete){
           // valueRepository.deleteByPersonIdAndAttrId(this.id,attr.getId());
        }
    }


    private void deleteAttributeValues(){

    }

    /*private void checkValues(PersonRepository personRepository){
        Person pers = personRepository.findOne(this.id);
        HashMap<String,Object> currentValues= this.parseValues(pers.getValues());
        ArrayList<String> itemsToDelete = new ArrayList<String>();
        ArrayList<String> itemsToUpdate = new ArrayList<String>();
        ArrayList<String> itemsToAdd = new ArrayList<String>();

        for(String val:currentValues.keySet()){
            if(this.attributes.containsKey(val)){
                itemsToUpdate.add(val);
            }
            else{
                itemsToDelete.add(val);
            }
        }
    }*/




}
