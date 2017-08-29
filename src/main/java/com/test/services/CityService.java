package com.test.services;

import com.test.entities.City;

public interface CityService {

    public Iterable<City> getAllCities();
    public City saveCity(City city);
    public void deleteCityById(Long id,Boolean force);
    public City findCityById(Long id);
    public City updateCityById(Long cityId,City city);

}
