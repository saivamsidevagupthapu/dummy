package com.stablespringbootproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stablespringbootproject.Entity.Countryentity;

public interface Countryrepo extends JpaRepository<Countryentity, Long> {

    Optional<Countryentity> findByCountryCode(String countryCode);

}