package com.stablespringbootproject.Dto;

import java.util.List;

public class Stablerequest {

	private String vehicleno;
	private String country;
	private String vendorname;
	private String phone_number;
	private String api_usage_type;
	private List<String> fields; // e.g. ["make", "model"] — if empty or null, all fields returned

	public String getVehicleno() {
		return vehicleno;
	}

	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getVendorname() {
		return vendorname;
	}

	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getApi_usage_type() {
		return api_usage_type;
	}

	public void setApi_usage_type(String api_usage_type) {
		this.api_usage_type = api_usage_type;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

}