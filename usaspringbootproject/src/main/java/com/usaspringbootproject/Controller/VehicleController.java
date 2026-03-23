package com.usaspringbootproject.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.usaspringbootproject.Entity.VehicleEntity;
import com.usaspringbootproject.Service.Vehicleservice;

@RestController
@RequestMapping("/usa")
public class VehicleController {

    private final Vehicleservice vehicleService;

    public VehicleController(Vehicleservice vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/vehicle/{vendorname}/{platenumber}")
    public VehicleEntity getVehicle(@PathVariable String vendorname,@PathVariable String platenumber) {

        System.out.println("Vendor: " + vendorname);
        System.out.println("Plate: " + platenumber);
        return vehicleService.getVehicleByNumber(platenumber, vendorname);
    }

    @GetMapping("/vehicles")
    public List<VehicleEntity> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @PostMapping("/vehiclesby")
    public VehicleEntity saveVehicle(@RequestBody VehicleEntity vehicle) {
        return vehicleService.saveVehicle(vehicle);  
    }
    @GetMapping("/vehicles/{vendorname}/{platenumber}")
    public VehicleEntity getVehicleByParams(
            @RequestParam String vendorname,
            @RequestParam String platenumber) {

        System.out.println("Vendor: " + vendorname);
        System.out.println("Plate: " + platenumber);

        return vehicleService.getVehicleByNumber(platenumber, vendorname);
    }
    
}