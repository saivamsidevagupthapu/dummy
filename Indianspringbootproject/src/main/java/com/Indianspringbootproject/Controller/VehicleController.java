package com.Indianspringbootproject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Indianspringbootproject.Entity.VehicleInfo;
import com.Indianspringbootproject.service.VehicleService;
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService service;

    @PostMapping
    public VehicleInfo createVehicle(@RequestBody VehicleInfo vehicle) {
        return service.saveVehicle(vehicle);
    }

    @GetMapping("/{vendorname}/{vehiclenumber}")
    public VehicleInfo getVehicle(@PathVariable String vehiclenumber,
                                 @PathVariable String vendorname) {
        return service.getVehicleInfo(vehiclenumber, vendorname);
    }

    @GetMapping("/detailsByVendor")
    public VehicleInfo detailsByVendor(@RequestParam("plateNumber") String plateNumber,
                                       @RequestParam("vendorname") String vendorname) {
        return service.getVehicleInfo(plateNumber, vendorname);
    }

    @GetMapping("/vechidetails")
    public VehicleInfo vechidetails(@RequestParam("vno") String vehiclenumber,
                                   @RequestParam("ownerName") String ownerName) {
        return service.getVehicleInfo(vehiclenumber, ownerName);
    }

    @GetMapping("/header")
    public VehicleInfo getVehicleFromHeaders(
            @RequestHeader("vehiclenumber") String vehiclenumber,
            @RequestHeader("vendorname") String vendorname) {

        return service.getVehicleInfo(vehiclenumber, vendorname);
    }

    @PostMapping("/body")
    public VehicleInfo getVehicleFromBody(@RequestBody VehicleInfo request) {
        return service.getVehicleInfo(
                request.getVehicleNumber(),
                request.getVendorname()
        );
    }

    @GetMapping("/query")
    public VehicleInfo getVehicleFromQuery(
            @RequestParam("vehicleno") String vehicleno,
            @RequestParam("vendorname") String vendorname) {

        return service.getVehicleInfo(vehicleno, vendorname);
    }
}
