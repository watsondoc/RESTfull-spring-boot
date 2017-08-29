package com.test.controllers;


import com.test.exceptions.PersonNameException;
import com.test.jsonentities.JsonInPerson;
import com.test.jsonentities.JsonOutPerson;
import com.test.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/api")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping(path="/persons")
    public @ResponseBody
    List<JsonOutPerson> getAllPersons(@RequestParam(value = "page", required=false) Integer page, @RequestParam(value = "page_entries", required=false) Integer page_entries, @RequestParam(value = "sort", required=false) String sortStr, @RequestParam(value = "filters", required=false) String filters) throws IOException {
        return personService.getAllPersons(page,page_entries,sortStr,filters);
    }

    @GetMapping(path="/persons/count")
    public @ResponseBody
    HashMap<String,Long> getPersonCount(){
        HashMap<String,Long> countJson = new HashMap<String,Long>();
        countJson.put("count",personService.getPersonsCount());
        return countJson;
    }

    @GetMapping(value="/persons/{person_id}")
    public @ResponseBody  List<JsonOutPerson> findId(@PathVariable Long person_id){
        List<JsonOutPerson> mOutPersons = new ArrayList<JsonOutPerson>();
        mOutPersons.add(personService.findById(person_id));
        return mOutPersons;
    }

    @DeleteMapping(value="/persons/{person_id}")
    public @ResponseBody String deletePerson(@PathVariable Long person_id){
        personService.deletePersonById(person_id);
        return "Delete complete";
    }

    @PostMapping(path="/persons")
    public @ResponseBody  JsonOutPerson createPerson(@RequestBody JsonInPerson person) {
        if(person.getName()==null || person.getName().equals(""))
            throw new PersonNameException();

        return personService.createPerson(person);
    }

    @PatchMapping(value="/persons/{person_id}")
    public @ResponseBody JsonOutPerson updatePerson(@PathVariable Long person_id, @RequestBody JsonOutPerson person) {
        return personService.updatePerson(person_id,person);
    }

}
