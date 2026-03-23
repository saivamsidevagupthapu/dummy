package com.stablespringbootproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.stablespringbootproject.Entity.Vendorapis;

public interface Vendorapirepo extends JpaRepository<Vendorapis, Long> {

    // Get all APIs by vendor name
    @Query(value =
        "SELECT va.* FROM vendor_apis va " +
        "JOIN vendorentity v ON va.vendor_id = v.vendor_id " +
        "WHERE v.vendor_name = :vendorName",
        nativeQuery = true)
    List<Vendorapis> findApisByVendorName(@Param("vendorName") String vendorName);


    // Get API by vendorId and apiType
    @Query(value =
        "SELECT * FROM vendor_apis " +
        "WHERE vendor_id = :vendorId " +
        "AND api_type = :apiType",
        nativeQuery = true)
    List<Vendorapis> findByVendorIdAndApiType(
            @Param("vendorId") Long vendorId,
            @Param("apiType") String apiType
    );
}