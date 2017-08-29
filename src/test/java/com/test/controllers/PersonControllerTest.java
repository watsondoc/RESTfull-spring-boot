package com.test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.Application;
import com.test.entities.City;
import com.test.entities.Person;
import com.test.repositories.CityRepository;
import com.test.repositories.PersonRepository;
import com.test.services.PersonService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    PersonService personService;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    CityRepository cityRepository;


    @Autowired
    private MockMvc mvc;

    @Before
    public void multipleInit() {
        /*JsonInPerson person = new JsonInPerson();
        person.setName("Name -----");
        person.setAge(10);
        personService.createPerson(person);*/
        personRepository.deleteAll();
        cityRepository.deleteAll();
    }

    @BeforeClass
    public static void initAll() {

    }

    @Test
    public void getAllPersons() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/persons").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(content().json("[]"));
    }


    @Test
    public void getPersonCount() throws Exception {
        Integer personsCount = 5;
        for(int i=0;i<personsCount;i++){
            Person person = new Person();
            person.setName("name" + i);
            personRepository.save(person);
        }
        mvc.perform(MockMvcRequestBuilders.get("/api/persons/count").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.count",is(personsCount)));
    }

    @Test
    public void findId() throws Exception {
        Person person = new Person();
        person.setName("testName");

        personRepository.save(person);
        Long personId = person.getId();

        mvc.perform(MockMvcRequestBuilders.get("/api/persons/"+personId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id",is(personId.intValue())));
    }

    @Test
    public void deletePerson() throws Exception {
        Person person = new Person();
        person.setName("name");
        person.setAge(100);
        personRepository.save(person);
        Long personId = person.getId();
        mvc.perform(MockMvcRequestBuilders.delete("/api/persons/"+personId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Delete complete"));

    }

    @Test
    public void createPersonWithEmptyName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Person person = new Person();
        person.setAddress("address");
        person.setAge(25);

        String jsonInString = mapper.writeValueAsString(person);

        mvc.perform(MockMvcRequestBuilders.post("/api/persons").contentType(MediaType.APPLICATION_JSON)
                .content(jsonInString))
                .andExpect(status().isBadRequest());
    }

    @Test public void createPerson() throws  Exception{
        ObjectMapper mapper = new ObjectMapper();

        Person person = new Person();
        person.setName("name");
        person.setAge(25);

        String jsonInString = mapper.writeValueAsString(person);

        mvc.perform(MockMvcRequestBuilders.post("/api/persons").contentType(MediaType.APPLICATION_JSON)
                .content(jsonInString))
                    .andExpect(status().isOk());
    }

    @Test
    public void updatePerson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Person person = new Person();
        person.setName("name");
        person.setAge(100);

        personRepository.save(person);
        String newName = "newName";
        Integer newAge = 50;
        person.setName(newName);
        person.setAge(newAge);
        String jsonInString = mapper.writeValueAsString(person);

        mvc.perform(MockMvcRequestBuilders.post("/api/persons").contentType(MediaType.APPLICATION_JSON)
                .content(jsonInString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(newName)))
                .andExpect(jsonPath("$.age",is(newAge)));
    }

    @Test
    public void getPersonsByFilter() throws Exception {
        City city = new City();
        city.setName("Ivanovo");
        cityRepository.save(city);

        Person person = new Person();
        person.setName("testName");
        person.setCity(city);
        personRepository.save(person);

        String params = "filters={\"city\":\""+city.getName()+"\"}";
        String filterParams = URLEncoder.encode(params, "UTF-8");

        mvc.perform(MockMvcRequestBuilders.get("/api/persons/?"+filterParams))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].City",is(city.getName())));
    }

    @Test
    public void getPersonsByPaging() throws Exception{

        Integer personCount = 12;
        for(int i=0;i<personCount;i++){
            Person person = new Person();
            person.setName("testName");
            personRepository.save(person);
        }

        mvc.perform(MockMvcRequestBuilders.get("/api/persons?page=3&page_entries=5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void getPersonsByOrder() throws Exception{

        ArrayList<String> namesArray = new ArrayList<String>();
        Integer personCount = 10;
        Person person;
        for(int i=0;i<personCount;i++){
            Integer randomNum = (int)(Math.random() * 100);
            namesArray.add("name" + randomNum.toString());
            person = new Person();
            person.setName(namesArray.get(i));
            personRepository.save(person);
        }

        Collections.sort(namesArray, new Comparator<String>() {
            @Override
            public int compare(String str2, String str1)
            {
                return  str2.compareTo(str1);
            }
        });

        ResultActions actions = mvc.perform(MockMvcRequestBuilders.get("/api/persons?sort=name"));

        actions = actions.andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$", hasSize(personCount)));

        for(Integer i=0;i<personCount;i++) {
            actions = actions.andExpect(jsonPath("$["+i.toString()+"].name", is(namesArray.get(i))));
         }
    }
}