package com.usaspringbootproject.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usaspringbootproject.Entity.VehicleEntity;
import com.usaspringbootproject.Repository.VehicleRepository;

@Service
public class Vehicleservice {

    private final VehicleRepository repo;

    public Vehicleservice(VehicleRepository repo) {
        this.repo = repo;
    }

    public VehicleEntity getVehicleByNumber(String vehicleNumber,String vendorname) {
        return repo.findByVehicleNumberAndVendorname(vehicleNumber,vendorname);
    
    }
    public List<VehicleEntity> getAllVehicles() {
        return repo.findAll();
    }

    public VehicleEntity saveVehicle(VehicleEntity vehicle) {
        return repo.save(vehicle);
    }
}