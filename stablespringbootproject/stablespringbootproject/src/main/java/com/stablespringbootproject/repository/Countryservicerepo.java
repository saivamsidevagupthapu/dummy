package com.stablespringbootproject.repository;

	import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stablespringbootproject.Entity.Countryserviceentity;

	@Repository
	public interface Countryservicerepo extends JpaRepository<Countryserviceentity, Long> {

	    List<Countryserviceentity> findByActiveTrue();

	    List<Countryserviceentity> findByCountryCodeAndActiveTrue(String countryCode);
	    
	    Optional<Countryserviceentity> findFirstByCountryCodeAndActiveTrue(String countryCode);


	}
	
	