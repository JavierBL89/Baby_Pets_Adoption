/**
 * Javier Bastande
 */
package com.example.bb_pets_adoption;
import com.example.bb_pets_adoption.auth.controller.JwtUtil;
import com.example.bb_pets_adoption.auth.controller.OAuth2FailureHandler;
import com.example.bb_pets_adoption.auth.controller.OAuth2SuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * SecurityConfiguration class holds the security configuration settings for the application.
 * 
 * It defines how HTTP requests should be secured, including which endpoints are accessible for public
 * and which require authentication. It also sets up password encoding to strengthen password security.
 */

@Configuration   // Indicates that this class is a cofiguration class and might contain one or more bean methods annotated with @Bean which creates beans managed by Spring's container
@EnableWebSecurity
public class SecurityConfiguration  extends SimpleUrlAuthenticationSuccessHandler{
	
	// vars
	@Autowired
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	
	@Autowired
	private final OAuth2FailureHandler oAuth2FailureHandler;
	
	/**
	 * Constructor injection for oAuth2SuccessHandler, oAuth2SuccessHandler
	 * */
	public SecurityConfiguration(@Lazy OAuth2SuccessHandler oAuth2SuccessHandler, @Lazy OAuth2FailureHandler oAuth2FailureHandler) {
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oAuth2FailureHandler = oAuth2FailureHandler;
    }
 
	 
	/** 
	* Configure the security filter chain for handling HTTP security
	* Configures authorization for HTTP requests:
	*    - Permit everyone to access the permitAll() endpoints
	*    - Requires authentication for any other request; anyRequest().authenticated() 
	*    - Configures session management to be stateless; sessionManagement()
	*    - Configures OAuth2 resource server with default JWT handling; oauth2ResourceServer()
	**/
    
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		System.out.println("Configuring SecurityFilterChain"); // Log statement
		
		 
        System.out.println("OAuth2SuccessHandler instance created");

		    http
                    .cors(Customizer.withDefaults()) // Enable CORS
	                .csrf(csrf -> csrf.disable())
	                .authorizeHttpRequests(auth -> auth
	                        .requestMatchers("/auth/register/**", "/auth/forgot_password/**",
	                                "/auth/login/**", "/auth/logout/**", "/auth/reset_password/**", "/oauth2/**", "/login").permitAll()
	                        .anyRequest().authenticated()
	                )
	                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .oauth2Login(oauth2 -> oauth2
	                         
	                        .loginPage("http://localhost:3000/login")
	                        .successHandler(oAuth2SuccessHandler)
	                        .failureHandler(oAuth2FailureHandler)
	                        .failureUrl("http://localhost:3000/login?error=true")
	                        .authorizationEndpoint(authorization -> authorization
	                                .baseUri("/oauth2/authorization")
	                        )
	                )
			        .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
			        .logout(logout -> logout
			        		.logoutUrl("http://localhost:3000/home")
			        		.logoutSuccessUrl("http://localhost:3000/home")
			        		.invalidateHttpSession(true)
			        		.deleteCookies("JSESSIONID")
			        		);
	        System.out.println("SecurityFilterChain configured");

			        return http.build();
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
		 System.out.println("Creating Auth0 JwtDecoder");
        return NimbusJwtDecoder.withJwkSetUri("https://dev-x8jau2ioykz07t1t.us.auth0.com/.well-known/jwks.json").build();
    }
	
	
	/**
     * Bean for decoding incomming Google OAuth2 tokens with JwtDecoder 
     */
	@Bean
    public JwtDecoder googleJwtDecoder() {
		 System.out.println("Creating Google JwtDecoder");
        return NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();
    }
	
	/**
     * Bean for generating jwt
     */
	 @Bean
	    public JwtUtil jwtTokenUtil() {
	        return new JwtUtil();  
	 }
}
