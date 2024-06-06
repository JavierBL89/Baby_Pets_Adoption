/**
 * 
 */
package com.example.bb_pets_adoption.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service class for sending emails.
 */
@Service
public class EmailService {

	//objects
	@Autowired
	private JavaMailSender  mailSender;
	
	 /**
     * Sends an email.
     * 
     * @param to the recipient's email address
     * @param subject the subject of the email
     * @param body the body of the email
     */
	public void sendEmail(String to, String subject, String body) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		this.mailSender.send(message);
	}
}
