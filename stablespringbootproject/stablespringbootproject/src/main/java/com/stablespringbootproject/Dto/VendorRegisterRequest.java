package com.stablespringbootproject.Dto;

public class VendorRegisterRequest {
    private String vendorName;
    private String vendorPhone;

    private String countryCode;
    private String baseUrl;
    private String apiUsageType;

    private String apiUrl;

    private String vehicleNumberField;
    private String makeField;
    private String modelField;
    private String yearField;
    private String api_type;

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorPhone() {
        return vendorPhone;
    }

    public void setVendorPhone(String vendorPhone) {
        this.vendorPhone = vendorPhone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiUsageType() {
        return apiUsageType;
    }

    public void setApiUsageType(String apiUsageType) {
        this.apiUsageType = apiUsageType;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getVehicleNumberField() {
        return vehicleNumberField;
    }

    public void setVehicleNumberField(String vehicleNumberField) {
        this.vehicleNumberField = vehicleNumberField;
    }

    public String getMakeField() {
        return makeField;
    }

    public void setMakeField(String makeField) {
        this.makeField = makeField;
    }

    public String getModelField() {
        return modelField;
    }

    public void setModelField(String modelField) {
        this.modelField = modelField;
    }

    public String getYearField() {
        return yearField;
    }

    public void setYearField(String yearField) {
        this.yearField = yearField;
    }

	public Object getapi_type() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getApi_type() {
		return api_type;
	}

	public void setApi_type(String api_type) {
		this.api_type = api_type;
	}
}
//i have the entity file like this