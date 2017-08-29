package com.test.restclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.entities.City;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class CityHttpClient {
    private HttpClient client;
    private HttpGet request;
    private HttpResponse response;
    private final String nodeServerPath = "http://localhost:8000/api/";

    public CityHttpClient(){
        client = new DefaultHttpClient();
    }

    public Iterable<City> getCities(String path) throws IOException {
        request = new HttpGet(nodeServerPath+path);
        response = client.execute(request);
        //BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String responseBody = EntityUtils.toString(response.getEntity());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<City[]> cityResponce = restTemplate.getForEntity(nodeServerPath+path, City[].class);

        List<City> list = Arrays.asList(cityResponce.getBody());

        int getStubStatusCode = cityResponce.getStatusCode().value();
        if (getStubStatusCode < 200 || getStubStatusCode >= 300) {
            return null;
        }
        else{
            //ObjectMapper mapper = new ObjectMapper();
            //City[] cities= mapper.readValue(responseBody, City[].class);
            //list = mapper.readValue(responseBody, new TypeReference<List<City>>() { });
        }
        return list;
    }

    public City getCityById(String path) throws IOException {
        City city = null;

        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "http://localhost:8080/spring-rest/foos";
        ResponseEntity<City> response = restTemplate.getForEntity(nodeServerPath+path, City.class);
        city = restTemplate.getForObject(nodeServerPath+path, City.class);
        //response.
        /*request = new HttpGet(nodeServerPath+path);
        response = client.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());*/
        int getStubStatusCode = response.getStatusCode().value();
        if (getStubStatusCode < 200 || getStubStatusCode >= 300) {
            return null;
        }
        else{
            //ObjectMapper mapper = new ObjectMapper();
            //city= response.getBody();//mapper.readValue(responseBody, City.class);
        }

        return city;
    }

}
