package com.stablespringbootproject.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stablespringbootproject.Entity.Countryserviceentity;
import com.stablespringbootproject.repository.Countryservicerepo;

@Service
public class CountryServiceRegistry {

    private final Countryservicerepo repository;

    public CountryServiceRegistry(Countryservicerepo repository) {
        this.repository = repository;
    }

    public List<Countryserviceentity> getAllActiveServices() {
        return repository.findByActiveTrue();
    }
}