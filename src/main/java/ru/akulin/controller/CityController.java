package ru.akulin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.akulin.entity.City;
import ru.akulin.exception.CityControllerException;
import ru.akulin.repository.CityRepository;

@RestController
@RequestMapping(path = "/city")
public class CityController {

    @Autowired
    CityRepository cityRepository;

    @GetMapping(path = "")
    public Iterable<City> get() {
        try {
            return cityRepository.findAll();
        }
        catch (Exception cause) {
            throw new CityControllerException("Во время получения данных произошла ошибка.", cause);
        }
    }
}
