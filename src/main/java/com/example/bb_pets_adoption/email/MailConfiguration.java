/**
 * 
 */
package com.example.bb_pets_adoption.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Configuration class for creating and configuring JavaMailSender bean.
 * This configuration is needed to be able to send emails from the application.
 * 
 * Class fields Use @Value to inject the value of application.properties email configuartions and use credentials securely
 */

@Configuration  // Indicates that this class is a cofiguration class and might contain one or more bean methods annotated with @Bean which creates beans managed by Spring's container.
public class MailConfiguration {

	@Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;
    
	/**
     * Creates and configures a JavaMailSender bean.
     * 
     * @return a configured JavaMailSender instance
     */
    @Bean
    public JavaMailSender javaMailSender() {
    	
    	// create a new instance of JavaMailSenderImpl
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        
        // set the mail server host and port
        mailSender.setHost(this.host);
        mailSender.setPort(this.port);

        // set the username and password for the mail server
        mailSender.setUsername(this.username);
        mailSender.setPassword(this.password);

        // additional mail properties
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
