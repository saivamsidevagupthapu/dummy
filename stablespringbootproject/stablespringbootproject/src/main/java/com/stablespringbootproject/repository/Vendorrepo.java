package com.stablespringbootproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stablespringbootproject.Entity.Vendorentity;

public interface Vendorrepo extends JpaRepository<Vendorentity, Long> {
	Vendorentity findByVendorName(String vendorname);

	Vendorentity findByVendorNameIgnoreCaseAndPhoneNumber(String vendorname, String phoneNumber);
	

} 