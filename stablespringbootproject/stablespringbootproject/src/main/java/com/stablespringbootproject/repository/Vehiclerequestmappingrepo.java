package com.stablespringbootproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stablespringbootproject.Entity.vehiclerequestmapping;

@Repository
public interface Vehiclerequestmappingrepo extends JpaRepository<vehiclerequestmapping, Long> {

    List<vehiclerequestmapping> findByVendorIdAndApiId(Long vendorId, Long apiId);
}