/**
 * 
 */
package com.example.bb_pets_adoption;

import com.example.bb_pets_adoption.pet_listing.model.Cat;
import com.example.bb_pets_adoption.pet_listing.model.Dog;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.jsontype.NamedType;


/**
 * Class to configure Jackson polymorphic deserialization using the _class field present in pets JSON data. 
 * 
 * Since pet types(Cat, Dog) inherit from Pet abstarct class, we need to registered each obejct document with the concrete class reference to distinguish it from Pet, 
 * otherwise Pet abstarct class is attempted to be initialized and abstract classes cannot be instantiated  
 * 

 */
@Configuration
public class JacksonConfiguration {

	@Bean
	public ObjectMapper objectMapper() {

		ObjectMapper mapper = new ObjectMapper(); // used for reading and writing JSON data.
		mapper.registerModule(new JavaTimeModule()); // JavaTimeModule is a module that adds support for the Java 8 Date and Time API 
		                                             // (java.time). This module provides serializers and deserializers for LocalDate, 
		                                             // LocalDateTime, Instant, etc., allowing Jackson to correctly handle these types
		
		// these lines register the Cat and Dog classes as subtypes of Pet
		mapper.registerSubtypes(new NamedType(Cat.class, "Cat"));
		mapper.registerSubtypes(new NamedType(Dog.class, "Dog"));
		
		return mapper;      // the customized ObjectMapper is returned and will be used by the Spring application for JSON data processing
		
	}
}
