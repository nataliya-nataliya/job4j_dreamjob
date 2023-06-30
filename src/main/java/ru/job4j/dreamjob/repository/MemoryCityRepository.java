package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MemoryCityRepository implements CityRepository {
    private final Map<Integer, City> cities = new HashMap<>() {
        {
            put(1, new City(1, "City 1"));
            put(2, new City(2, "City 2"));
            put(3, new City(3, "City 3"));
        }
    };

    @Override
    public Collection<City> findAll() {
        return cities.values();
    }
}
