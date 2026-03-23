package com.Indianspringbootproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Indianspringbootproject.Entity.VehicleInfo;

public interface VehicleRepository extends JpaRepository<VehicleInfo, Long> {

	VehicleInfo findByVendorname(String vendorname);
    VehicleInfo findByVehicleNumberAndVendorname(String vehicleNumber, String vendorname);

}
