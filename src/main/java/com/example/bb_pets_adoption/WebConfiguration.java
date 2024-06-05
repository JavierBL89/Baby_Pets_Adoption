/**
 * 
 */
package com.example.bb_pets_adoption;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class implements WebMvcConfigurer interfacee
 * to configurer Cross-Origin Resource Sharing (CORS)
 * It allows the frontend application running on a different URL to access the backend API 
 * for HTTP requests
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer{
	
	/**
     * Bean to configure CORS settings.
     * 
     */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")                 // apply CORS settings to all endpoints in app
		    .allowedOrigins("http://localhost:3000")   // allow requests from this origin
		    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // allow these HTTP methods
		    .allowedHeaders("*")                       // allow all headers
		    .allowCredentials(true);                   // allow credentials (cookies, authorization headers, etc.)
	}
}
