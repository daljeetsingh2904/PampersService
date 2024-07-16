package com.pampers.GenericPampersService.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pampers.GenericPampersService.dao.DbHandler;
import com.pampers.GenericPampersService.model.ServicConfigEntity;
import com.pampers.GenericPampersService.repository.ServiceConfiguration;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import java.lang.reflect.Method;


@Service
public class ServiceExecutor {
	
	
	 @Autowired
	    private ServiceConfiguration serviceRepo;

//      @Autowired
//      private ServicConfigEntity service;
     
	    @Autowired
	    private DbHandler dbHandler;
	    
	    @Autowired
	    PampersApplication pampersApp;

	    public void executeService(String serviceName) throws Exception {
	       ServicConfigEntity service=new ServicConfigEntity();
	       service.setServiceName(serviceName);

	    	service = dbHandler.fetchConfiguration(service);

	    	Optional<ServicConfigEntity> configOpt = serviceRepo.findByServiceName(serviceName);

	    	if (configOpt.isPresent()) {
	    	    System.out.println("config opt is " + configOpt);
	    	    ServicConfigEntity config = configOpt.get();
	    	    service.setAmiUsername(config.getAmiUsername());
	    	    service.setAmiPassword(config.getAmiPassword());
	    	    service.setAmiHostname(config.getAmiHostname());
	    	    service.setCallStatus(config.getCallStatus());
	    	    service.setObdRingTimeout(config.getObdRingTimeout());
	    	    service.setContextName(config.getContextName());
	    	    service.setCallerId(config.getCallerId());
	    	    service.setObdThreadSleepTime(config.getObdThreadSleepTime());
	    	    service.setObdCallParameters(config.getObdCallParameters());
	    	    service.setSalt(config.getSalt());
	    	    service.setSecretKey(config.getSecretKey());
	    	} else {
	    	    System.out.println("value not set");
	    	}

	    	System.out.println("service name is " + capitalize(serviceName));
	    	Class<?> callingClass = Class.forName("com.pampers.GenericPampersService.services." + capitalize(serviceName) + "Application");
	    	Object obj = callingClass.getDeclaredConstructor().newInstance();
	    	Method method = callingClass.getDeclaredMethod("callInitiator", ServicConfigEntity.class, DbHandler.class);
	    	method.invoke(obj, service, dbHandler);
	    }

	    private String capitalize(String str) {
	        if (str == null || str.isEmpty()) {
	            return str;
	        }
	        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	    }
}
