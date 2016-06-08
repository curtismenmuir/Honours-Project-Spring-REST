package test_app.controllers;

import test_app.models.PasswordReset;
import test_app.models.PasswordResetDao;
import test_app.models.User;
import test_app.models.UserDao;
import test_app.lib.PaswordConvertorSHA256;
import test_app.lib.EmailHandler;
import org.springframework.http.HttpStatus;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This Controller will handle URL requests for reseting user passwords. The Users 
 * will be emailed a link to a reset password page in the app. A token is created 
 * for added security.  
 * 
 * @author Dreads
 */
@Controller
public class PasswordResetController {
	
	/**
	 * This method will search for a reset password record with a specific token.
	 * 
	 * @param token
	 * @return PasswordResetRecord (if found) or null
	 */
	@RequestMapping(value = "/get-resetrecord-by-token", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public PasswordReset getByToken(String token){
		try{
			PasswordReset passwordReset = new PasswordReset();
			passwordReset = passwordResetDao.findByToken(token);
			return passwordReset;
		}
		catch(Exception ex){
			return null;
		}
	}
	/** 
	 * This method will search for a reset password record registered to a specific
	 * user.
	 * 
	 * @param username
	 * @return PasswordResetRecord (if found) or null
	 */
	@RequestMapping(value = "/get-user-reset-record", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public PasswordReset getByUsername(String username){
		try{
			PasswordReset passwordReset = new PasswordReset();
			passwordReset = passwordResetDao.findByUsername(username);
			return passwordReset;
		}
		catch(Exception ex){
			return null;
		}
	}
	/**
	 * This method will create a Password Reset Record for a specific user. It will generate a token 
	 * from a random UUID, username and timeDateRequested.
	 * 
	 * @param username
	 * @return Ture/False
	 */
	@RequestMapping(value = "/create-password-reset-record", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public boolean createPasswordResetRecord(String username){
		try{
			User user = userDao.findByUsername(username);
			String userEmail = user.getEmail();
			String token = java.util.UUID.randomUUID().toString();
	        Timestamp dateTimeRequested = new Timestamp(new java.util.Date().getTime());
	     
	        String hashedToken = PaswordConvertorSHA256.SHA256(username + token);
	        PasswordReset passwordReset = new PasswordReset(hashedToken, username, dateTimeRequested);
	        passwordResetDao.save(passwordReset);
	        
	        final String emailAddressTo = userEmail;
	        final String emailSubject = "Passwort Reset";
	        final String link = "https://qmb_booking_app/" + username + "/" + username + token;
	        final String emailMessage = "<p>UoDb password reset request for " + username + "</p><p>Please open the app when clicking on the link below. Opening it in a broswer will not allow you to reset your password.</p><p>You have 24 hours to access the link below before it becomes invalid!</p>" + "<a href='" + link + "\'>" + link + "</a><p>If your phone does not allow Deep Linking, please copy the above link into the application.</p>";
	        EmailHandler.SendEmail(emailAddressTo, emailSubject, emailMessage);
	        
			return true;
		}
		catch(Exception ex){
			return false;
		}		
	}
	/**
	 * This method will update a users Password Reset Record, so that a valid reset link
	 * will be emailed to the user, enabling them to reset their account password. 
	 * 
	 * @param username
	 * @return True/False
	 */
	@RequestMapping(value = "/update-password-reset-record", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public boolean updatePasswordResetRecord(String username){
		try{
			User user = userDao.findByUsername(username);
			String userEmail = user.getEmail();
			String token = java.util.UUID.randomUUID().toString();
	        Timestamp dateTimeRequested = new Timestamp(new java.util.Date().getTime());
	        String hashedToken = PaswordConvertorSHA256.SHA256(username + token + dateTimeRequested);

	        PasswordReset passwordReset = new PasswordReset();
			passwordReset = passwordResetDao.findByUsername(username);
			passwordReset.setToken(hashedToken);
			passwordReset.setDateTimeRequested(dateTimeRequested);
			passwordResetDao.save(passwordReset);
			
			final String emailAddressTo = userEmail;
	        final String emailSubject = "Passwort Reset";
	        final String link = "https://qmb_booking_app/" + username + "/" + username + token;
	        final String emailMessage = "<p>UoDb password reset request for " + username + "</p><p>Please open the app when clicking on the link below. Opening it in a broswer will not allow you to reset your password.</p><p>You have 24 hours to access the link below before it becomes invalid!</p>" + "<a href='" + link + "\'>" + link + "</a>";
	        EmailHandler.SendEmail(emailAddressTo, emailSubject, emailMessage);
			return true;
		}
		catch(Exception ex){
			return false;
		}
	}
	/**
	 * This method will delete a Password Reset Record for a specific user. 
	 * 
	 * @param username
	 * @return True/False
	 */
	@RequestMapping(value = "/delete-password-reset-record", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public boolean deleteResetRecord(String username){
		try{
			PasswordReset passwordReset = new PasswordReset();
			passwordReset = passwordResetDao.findByUsername(username);
			passwordResetDao.delete(passwordReset);
			return true; 
		}
		catch(Exception ex){
			return false; 
		}
	}
	
	@Autowired
	private PasswordResetDao passwordResetDao;
	
	@Autowired
	private UserDao userDao;
}
