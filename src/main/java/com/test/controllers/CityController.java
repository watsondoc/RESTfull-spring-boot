package com.test.controllers;

import com.test.repositories.CityRepository;
import com.test.entities.City;
import com.test.entities.Greeting;
import com.test.restclient.CityHttpClient;
import com.test.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/api")
public class CityController {

    CityHttpClient client;

    protected CityController(){
        client = new CityHttpClient();
    }

    @Autowired // This means to get the bean called cityRepository
    private CityService cityService;

    @GetMapping(path="/cities")
    public @ResponseBody Iterable<City> getAllCities() throws IOException {
        // This returns a JSON or XML with the users

        Iterable<City> mCities = cityService.getAllCities();//client.getCities("cities"); //Use RestTemplate
        return mCities;
    }

    @PostMapping(path="/cities")
    public @ResponseBody City createCity(@RequestBody City city) {
        return cityService.saveCity(city);
        //City mCity = userRepository.findAll();
    }

    @DeleteMapping(value="/cities/{cityId}")
    public @ResponseBody String deleteCity(@PathVariable Long cityId, @RequestParam(value = "force", required=false) Boolean force){
        cityService.deleteCityById(cityId,force);
        return "Deleting complete";
    }

    @GetMapping(value="/cities/{cityId}")
    public @ResponseBody City findId(@PathVariable Long cityId) throws IOException {
        //City city = client.getCityById("cities/"+cityId.toString()); //Use RestTemplate
        return cityService.findCityById(cityId);
    }

    @PutMapping(value="/cities/{cityId}")
    public @ResponseBody City updateCity(@PathVariable Long cityId, @RequestBody City city){
        return cityService.updateCityById(cityId,city);
    }
}