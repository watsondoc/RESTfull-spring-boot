package com.test.services.impl;

import com.test.entities.City;
import com.test.exceptions.PersonsContainsEntity;
import com.test.repositories.CityRepository;
import com.test.repositories.PersonRepository;
import com.test.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl implements CityService{

    @Autowired // This means to get the bean called cityRepository
    private CityRepository cityRepository;
    @Autowired // This means to get the bean called cityRepository
    private PersonRepository personRepository;

    public Iterable<City> getAllCities(){

        return cityRepository.findAll();
    }

    public City saveCity(City city){

        return cityRepository.save(city);
    }

    public void deleteCityById(Long id,Boolean force){

        City deletedCity = cityRepository.findOne(id);
        if(personRepository.countByCity(deletedCity)>0){
            if(force!=null && force){
                personRepository.deleteCities(id);
                cityRepository.delete(deletedCity);
            }
            else
            {
                throw new PersonsContainsEntity();
            }
        }
        else{
            cityRepository.delete(deletedCity);
        }
    }

    public City findCityById(Long id){

        return cityRepository.findOne(id);
    }

    public City updateCityById(Long cityId,City city){
        City dbCity = cityRepository.findOne(cityId);
        String cname = city.getName();
        dbCity.setName(cname);
        return cityRepository.save(dbCity);
    }

}
