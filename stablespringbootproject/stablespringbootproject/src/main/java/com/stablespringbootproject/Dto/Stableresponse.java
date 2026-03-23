package com.stablespringbootproject.Dto;

import java.util.Map;

public class Stableresponse {

	private String country;

	// Optional: generic map holder used by Stableservice.mapVendorResponse(...)
	private Map<String, String> vehicleDetails;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Map<String, String> getVehicleDetails() {
		return vehicleDetails;
	}

	public void setVehicleDetails(Map<String, String> vehicleDetails) {
		this.vehicleDetails = vehicleDetails;
	}
}