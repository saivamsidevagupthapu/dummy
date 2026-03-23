package com.Indianspringbootproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Indianspringbootproject.Entity.VehicleInfo;
import com.Indianspringbootproject.Repository.VehicleRepository;

@Service
public class VehicleService {
	
	@Autowired
	private VehicleRepository repository;
	
	public VehicleInfo saveVehicle(VehicleInfo vehicle) {
        return repository.save(vehicle);
	}

	  public VehicleInfo getVehicleInfo(String vehicleNumber, String vendorname) {
		
	        return repository.findByVehicleNumberAndVendorname(vehicleNumber, vendorname);
	    }

	}
