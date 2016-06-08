package test_app.lib;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * This method will connect to the University's mail server
 * 
 * @author Dreads
 *
 */
public class EmailHandler {
	
	/**
	 * This method will send an Email on the universitys mail server. 
	 * 
	 * @param emailAddressTo
	 * @param emailSubject
	 * @param emailMessage
	 */
	public static void SendEmail(final String emailAddressTo, final String emailSubject, final String emailMessage)
    {
        // Sender's email ID and password needs to be mentioned
        final String from = "ccmenmuir@dundee.ac.uk";
        final String pass = "Passworduni123";

        // Defining the mail host
        String host = "smtp.office365.com";

        // Creating Properties object
        Properties props = new Properties();

        // Defining properties
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.port", "587");

        // Authorized the Session object.
        Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(mailSession);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(emailAddressTo));
            // Set Subject: header field
            message.setSubject(emailSubject);
            // Now set the actual message
            message.setContent(emailMessage, "text/html");
            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
