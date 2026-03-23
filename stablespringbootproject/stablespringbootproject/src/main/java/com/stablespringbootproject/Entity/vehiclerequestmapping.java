package com.stablespringbootproject.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehiclerequestmapping")
public class vehiclerequestmapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vendor_id")
    private Long vendorId;

    @Column(name = "api_id")
    private Long apiId;   // link to Vendorapis.apiId

    // which field in Stablerequest this row is for (VEHICLENO, COUNTRY, etc)
    @Column(name = "stable_field")
    private String stableField;

    // where to put it in vendor request: PATH / QUERY / HEADER / BODY_JSON
    @Enumerated(EnumType.STRING)
    @Column(name = "location")
    private Requestlocation location;

    // vendor’s name for this field:
    //  - PATH: placeholder in URL (vehicleNo)
    //  - QUERY: param name (vin)
    //  - HEADER: header name (X-API-KEY)
    //  - BODY_JSON: json key (vehicleNumber)
    @Column(name = "external_name")
    private String externalName;

    // optional: constant value, when vendor wants a fixed param/header
    @Column(name = "constant_value")
    private String constantValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public String getStableField() {
        return stableField;
    }

    public void setStableField(String stableField) {
        this.stableField = stableField;
    }

    public Requestlocation getLocation() {
        return location;
    }

    public void setLocation(Requestlocation location) {
        this.location = location;
    }

    public String getExternalName() {
        return externalName;
    }

    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }

    public String getConstantValue() {
        return constantValue;
    }

    public void setConstantValue(String constantValue) {
        this.constantValue = constantValue;
    }
}