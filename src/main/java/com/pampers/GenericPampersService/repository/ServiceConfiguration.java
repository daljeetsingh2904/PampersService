package com.pampers.GenericPampersService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pampers.GenericPampersService.model.ServicConfigEntity;

import java.util.Optional;


public interface ServiceConfiguration extends JpaRepository<ServicConfigEntity, Long> {
    @Query(value="SELECT * FROM _tbl_serviceconfiguration_details sc WHERE sc.serviceName = :serviceName" ,nativeQuery = true)
	Optional<ServicConfigEntity> findByServiceName(@Param("serviceName") String serviceName);
    
   }
