package com.pampers.GenericPampersService.repository;

public class KeyTerms {

	
	/*
	 * 1. Bean class - A bean is simply an object that is managed by spring ioc container , this 
	 *         means the container creates an object ,initializes it and manages its life cycle.
	 *         @Component class is example of bean class . The class annotaeted with @Component 
	 *         will be automatically detected and registered as spring bean during classpath scanning.
	 *        
	 *  2.Entity - Indicates that a class is entity and is mapped to database table.it is used in jpa to define 
	 *             persistence entity.The class should have an identifier field annotated with @Id
	 * 
	 *  3.@Component - Indicates that a class is spring managed component, not provide any additional functionality.
	 *  
	 *  4.@Service - specialization of component that indicated that annotated class is service ,adding more semantic 
	 *                meaning that class contain bussiness logic or service layer functionality. Does not add new behaviour 
	 *                beyond compoennt class.
	 *                
	 *  5.@Configuration - -> Indicates that a class can contain one or more @Beans method that can be processed by spring container 
	 *                    to generate bean definitions and service requests at runtime      
	 *                    -> It is used to define configuration classes that can contain methods annotated with @Bean to define beans
	 *                    -> These classes can also use other configuration annotations like @PropertySource or @ComponentScan
	 *                    
	 *  6.@Bean- it is used with @Configuration classes to define beans,The method annotated with @Bean will be called and 
	 *              its return value will be registered as bean with spring application context.
	 *              
	 *  7.Application Context - ApplicationContext is the central interface that provides configuration and lifecycle management for your application.
	 *                           It acts as a container for beans, which are the building blocks of your Spring application.
                               
	 *        
	 */
}

