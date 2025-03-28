/**
 * Javier Bastande
 */
package com.example.bb_pets_adoption;
import com.example.bb_pets_adoption.auth.controller.JwtUtil;
import com.example.bb_pets_adoption.auth.controller.OAuth2FailureHandler;
import com.example.bb_pets_adoption.auth.controller.OAuth2SuccessHandler;
import com.example.bb_pets_adoption.auth.service.CustomOAuth2UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
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
public class SecurityConfiguration {
	
	// vars
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	private final OAuth2FailureHandler oAuth2FailureHandler;
	private final CustomOAuth2UserService customOAuth2UserService;
	
	
	/**
     * Constructor injection for dependencies
     * 
     * @param oAuth2SuccessHandler the handler for OAuth2 authentication success
     * @param oAuth2FailureHandler the handler for OAuth2 authentication failure
     * @param customOAuth2UserService the service to load user information during OAuth2 authentication
     */
	@Autowired
	public SecurityConfiguration(@Lazy OAuth2SuccessHandler oAuth2SuccessHandler, @Lazy OAuth2FailureHandler oAuth2FailureHandler,  @Lazy CustomOAuth2UserService customOAuth2UserService) {
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oAuth2FailureHandler = oAuth2FailureHandler;
        this.customOAuth2UserService = customOAuth2UserService;
    }
 
	 
	/** 
	* Configure the security filter chain for handling HTTP security
	* Configures authorization for HTTP requests:
	*    - Permit everyone to access the permitAll() endpoints
	*    - Requires authentication for any other request; anyRequest().authenticated() 
	*    - Configures session management to be stateless; sessionManagement()
	*    - Configures OAuth2 resource server with default JWT handling; oauth2ResourceServer()
	*    
	* @param http the HttpSecurity object to configure
    * @return the configured SecurityFilterChain
    * @throws Exception if an error occurs
	**/ 
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		System.out.println("Configuring SecurityFilterChain"); // Log statement
		
		 
        System.out.println("OAuth2SuccessHandler instance created");

		    http
                    .cors(Customizer.withDefaults()) // Enable CORS
	                .csrf(csrf -> csrf.disable())	                
	                // allow public access to certain endpoints
	                .authorizeHttpRequests(auth -> auth
	                        .requestMatchers("/auth/register/**", "/auth/forgot_password/**", "/pets/kitties", "/pets/kitties/view/**", "/pets/puppies", "/pets/puppies/view/**",
	                        		"/pets/kitties/filter_by", "/pets/list_pet", "/pets/puppies/filter_by", "/auth/login/**", "/auth/logout/**", 
	                        		"/pets/my_listings", "/auth/reset_password/**", "/oauth2/**", "/login",
	                        		"/pets/delete_pet", "/pets/update_pet", "/adoption/apply", "/adoption/delete_application",
	                        		"/adoption/pet/applications/updateStatus", "/adoption/pet/applications", "/adoption/my_applications",
	                        		"/account_management/update_details", "/account_management/delete_user", "/account_management/update_email", "/notifications/pending",
	                        		"/notifications/markAsViewed").permitAll()
	                     
	                        // Secure other endpoints
	                        .requestMatchers(HttpMethod.POST, "/adoption").authenticated()
	                        .anyRequest().authenticated()
	                )
	                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .formLogin(form -> form
	                        .loginPage("http://localhost:3000/login")
	                        .defaultSuccessUrl("http://localhost:3000", true)
	                        .failureUrl("http://localhost:3000/login?error=true")
	                        .failureHandler(authenticationFailureHandler()) // customized failure handler
	                        
	                )
	                .oauth2Login(oauth2 -> oauth2
	                        .loginPage("http://localhost:3000/login")
	                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
	                        .successHandler(oAuth2SuccessHandler)
	                        .failureHandler(oAuth2FailureHandler)
	                        .authorizationEndpoint(authorization -> authorization.baseUri("/oauth2/authorization"))

	                        )
	                
			        .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
			        .logout(logout -> logout
			        		.logoutUrl("http://localhost:3000/**")
			        		.logoutSuccessUrl("http://localhost:3000/")
			        		.invalidateHttpSession(true)
			        		.deleteCookies("JSESSIONID")
			        		);
	        System.out.println("SecurityFilterChain configured");

			        return http.build();
	}
	
	
	@Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                                AuthenticationException exception) throws IOException, ServletException {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid email address or password. Please reset data entered and try again.");
            }
        };
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
     * 
     * @return the JwtDecoder for Auth0
     */
	@Bean
	@Primary
    public JwtDecoder auth0JwtDecoder() {
		 System.out.println("Creating Auth0 JwtDecoder");
        return NimbusJwtDecoder.withJwkSetUri("https://dev-x8jau2ioykz07t1t.us.auth0.com/.well-known/jwks.json").build();
    }
	
	
	/**
     * Bean for decoding incomming Google OAuth2 tokens with JwtDecoder 
     * 
     *  @return the JwtDecoder for Google
     */
	@Bean
    public JwtDecoder googleJwtDecoder() {
		 System.out.println("Creating Google JwtDecoder");
        return NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();
    }
	
	/**
     * Bean for generating jwt
     * 
     * @return the JwtUtil instance
     */
	 @Bean
	    public JwtUtil jwtTokenUtil() {
	        return new JwtUtil();  
	 }
}
