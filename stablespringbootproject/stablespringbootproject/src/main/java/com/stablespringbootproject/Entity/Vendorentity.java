package com.stablespringbootproject.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vendorentity")
public class Vendorentity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="vendor_id")
    private Long id;

    @Column(name = "vendor_name")
    private String vendorName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "active")
    private Boolean active;

    // getters and setters

    public Long getId() {
        return id;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Boolean getActive() {
        return active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}