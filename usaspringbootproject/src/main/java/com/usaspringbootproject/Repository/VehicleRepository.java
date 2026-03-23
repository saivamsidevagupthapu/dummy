package com.usaspringbootproject.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usaspringbootproject.Entity.VehicleEntity;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {

    Optional<VehicleEntity> findByVehicleNumber(String vehicleNumber);
    VehicleEntity findByVehicleNumberAndVendorname(String vehicleNumber, String vendorname);
}