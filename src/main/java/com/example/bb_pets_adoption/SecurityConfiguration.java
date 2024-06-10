/**
 * Javier Bastande
 */
package com.example.bb_pets_adoption;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * SecurityConfiguration class holds the security configuration settings for the application.
 * 
 * It defines how HTTP requests should be secured, including which endpoints are accessible for public
 * and which require authentication. It also sets up password encoding to strengthen password security.
 */

@Configuration   // Indicates that this class is a cofiguration class and might contain one or more bean methods annotated with @Bean which creates beans managed by Spring's container.
@EnableWebSecurity
public class SecurityConfiguration  {

	/** Configure the security filter chain for handling HTTP security
	* Configures authorization for HTTP requests:
	*    - Permit everyone to access the permitAll() endpoints
	*    - Requires authentication for any other request; anyRequest().authenticated() 
	*    - Configures session management to be stateless; sessionManagement()
	*    - Configures OAuth2 resource server with default JWT handling; oauth2ResourceServer()
	**/
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		
		  return http
				   // Disables CSRF protection (not recommended for production)
			        .csrf(csrf -> csrf.disable())
			        .authorizeHttpRequests(auth -> auth 
			        		// Permit everyone to access the bellow endpoints
	                        .requestMatchers("/auth/register/**", "/auth/forgot_password/**", 
	                        		"/auth/login/**", "/auth/reset_password/**", "/oauth2/**", "/login").permitAll()       			                
			                .anyRequest().authenticated() 
			        )
			        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			        .oauth2Login(oauth2 -> oauth2
	                        .loginPage("http://localhost:3000/home") // Point to your React login page
	                        .defaultSuccessUrl("http://localhost:3000/home", true) // Ensure this is the correct redirect URL
	                        .failureUrl("http://localhost:3000/login?error=true") // Handle login failure
	                        .authorizationEndpoint(authorization -> authorization
	                                .baseUri("/oauth2/authorization")
	                        )
	                    
	                )
			        .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())) 
			        .build();
	}
	
	
	/**
     * PasswordEncoder bean to use BCrypt for password hashing.
     * BCrypt is a strong hashing algorithm that strengthen password security.
     * 
     * @return the BCryptPasswordEncoder instance
     */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	/**
     * Bean necessary for decoding incomming JWT tokens from provider AuthO
     */
	@Bean
	@Primary
    public JwtDecoder auth0JwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("https://dev-x8jau2ioykz07t1t.us.auth0.com/.well-known/jwks.json").build();
    }
	
	
	/**
     * Bean for decoding incomming Google OAuth2 tokens with JwtDecoder 
     */
	@Bean
    public JwtDecoder googleJwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();
    }
}
