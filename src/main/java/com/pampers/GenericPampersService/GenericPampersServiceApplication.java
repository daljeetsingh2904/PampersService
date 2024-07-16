package com.pampers.GenericPampersService;

import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.pampers.GenericPampersService.services.ServiceExecutor;

@SpringBootApplication
@EntityScan(basePackages = "com.pampers.GenericPampersService.model")
@ComponentScan(basePackages = "com.pampers.GenericPampersService")

//@EnableJpaRepositories

public class GenericPampersServiceApplication implements CommandLineRunner  {

	  @Autowired
	  private ServiceExecutor serviceExecutor;
	  
	public static void main(String[] args) {
		SpringApplication.run(GenericPampersServiceApplication.class, args);
	}
	
	@Override
	 public void run(String... args) {
	   
//		if (args.length == 0) {
//	            System.out.println("Failed to Start the Application as ServiceName cannot be Null");
//	            return;
//	        }

	        try {
	        	System.out.println("you are in try");
	            serviceExecutor.executeService("pampers");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

}
