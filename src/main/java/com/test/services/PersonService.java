package com.test.services;

import com.test.jsonentities.JsonInPerson;
import com.test.jsonentities.JsonOutPerson;

import java.io.IOException;
import java.util.List;

public interface PersonService {

    public List<JsonOutPerson> getAllPersons(Integer page,Integer page_entries,String sortStr,String filters) throws IOException;
    public Long getPersonsCount();
    public JsonOutPerson findById(Long id);
    public void deletePersonById(Long id);
    public JsonOutPerson createPerson(JsonInPerson person);
    public JsonOutPerson updatePerson(Long id, JsonOutPerson person);

}
