package com.stablespringbootproject.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stablespringbootproject.Dto.Stablerequest;
import com.stablespringbootproject.Dto.Stableresponse;
import com.stablespringbootproject.Service.Stableservice;

@RestController
@RequestMapping("/stable")
public class Stablecontroller {

	private final Stableservice service;

	public Stablecontroller(Stableservice service) {
		this.service = service;
	}

	@PostMapping("/vehicle")
	public Stableresponse getVehicle(@RequestBody(required = false) Stablerequest request) {
		if (request == null) {
			request = new Stablerequest();
		}
		return service.fetchVehicle(request);
	}

	// Registers a brand new vendor — throws 409 if vendor already exists
//	@PostMapping("/vendor/register")
//	public String registerVendor(@RequestBody VendorRegisterRequest request) {
//		return service.registerVendor(request);
//	}
//
//	// Updates API config and response mapping for an existing vendor — throws 404 if vendor not found
//	@PutMapping("/vendor/update")
//	public String updateVendor(@RequestBody VendorRegisterRequest request) {
//		return service.updateVendor(request);
//	}
//
//	// Registers a country service (base URL) independently
//	@PostMapping("/vendor/country")
//	public String registerCountryService(@RequestBody VendorRegisterRequest request) {
//		return service.registerCountryService(request);
//	}
}