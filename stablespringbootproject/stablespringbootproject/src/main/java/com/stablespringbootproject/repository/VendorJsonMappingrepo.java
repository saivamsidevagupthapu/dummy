package com.stablespringbootproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stablespringbootproject.Entity.Vehicleresponcemapping;

public interface VendorJsonMappingrepo extends JpaRepository<Vehicleresponcemapping, Long> {

    List<Vehicleresponcemapping> findByVendorId(Long vendorId);

    List<Vehicleresponcemapping> findByApiId(Long apiId);

    List<Vehicleresponcemapping> findByCountryId(Long countryId);
}