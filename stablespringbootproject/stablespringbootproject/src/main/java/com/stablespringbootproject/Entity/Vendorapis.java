package com.stablespringbootproject.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vendor_apis")
public class Vendorapis {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long apiId;

	@Column(name = "vendor_id")
	private Long vendorId;

	
	@Column(name = "api_url")
	private String apiUrl;

	// e.g. VehicleInfo, StockInfo, CustomerInfo
	@Column(name = "Api_type")
	private String apiType;

	@Column(name = "http_method")
	private String httpMethod; // GET / POST / PUT...

	@Column(name = "content_type")
	private String contentType; // e.g. "application/json"

	public Long getApiId() {
		return apiId;
	}

	public String getApiType() {
		return apiType;
	}

	public void setApiType(String apiType) {
		this.apiType = apiType;
	}

	public void setApiId(Long apiId) {
		this.apiId = apiId;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}