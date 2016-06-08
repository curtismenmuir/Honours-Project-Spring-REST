package test_app.controllers;

import test_app.lib.PaswordConvertorSHA256;
import test_app.models.RoomBooking;
import test_app.models.RoomBookingDao;
import test_app.models.User;
import test_app.models.UserDao;

import org.springframework.http.HttpStatus;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This Controller will handle URL requests regarding the users of the system. 
 * 
 * @author Dreads
 */
@Controller
public class UserController {

	/**
	 * This method will update a users password
	 * 
	 * @param username
	 * @param password
	 * @return True/False
	 */
	@RequestMapping(value = "/update-user-password", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public boolean updateUserPassword(String username, String password){
		try{
			User user = new User();
			user = userDao.findByUsername(username);
			SecureRandom random = new SecureRandom();
	        byte[] salt = new byte[32];
	        random.nextBytes(salt);
	        String encodedPassword = null;   
			String hexSalt = null;
			encodedPassword = PaswordConvertorSHA256.getHash(password, salt);
            hexSalt = PaswordConvertorSHA256.convertToHex(salt);
			user.setPassword(encodedPassword);
			user.setSalt(hexSalt);
			userDao.save(user);
			return true;
		}
		catch(Exception ex){
			return false;
		}
	}
	
	/**
	 * This method will delete any of the users room bookings which are older than one year.
	 * 
	 * @param roomCode
	 */
	public void deleteOldBookings(String username){
		try{
			LinkedList<RoomBooking> roomBookingList = roomBookingDao.findAllByCreatorOrderByDateOfBookingAscStartTimeAsc(username);
			for(RoomBooking rb : roomBookingList){
				String date = rb.getDateOfBooking();
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
				Date dateOfBooking = sdf.parse(date);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.YEAR, -1);
				Date deleteDate = cal.getTime();
				if(dateOfBooking.before(deleteDate)){
					roomBookingDao.delete(rb);
				}				
			}
			
		}catch(Exception ex){
		}
	}
	
	
	/**
	 * This method will allow users to login to their accounts. 
	 * 
	 * @param username
	 * @param password
	 * @return True/False
	 */
	@RequestMapping(value = "/user-login", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public User loginUser(String username, String password){
		User user = new User();
		String accountpassword = "";
		String hexSalt = "";
		try
		{
			user = userDao.findByUsername(username);
			accountpassword = String.valueOf(user.getPassword());
			hexSalt = String.valueOf(user.getSalt());
			byte[] salt = PaswordConvertorSHA256.convertFromHex(hexSalt);
			String encodedPasswordAttempt = PaswordConvertorSHA256.getHash(password, salt);
			if(encodedPasswordAttempt.equals(accountpassword))
			{
				deleteOldBookings(username);
				return user;
			}
			else
			{
				return null;
			}
		}
		catch(Exception ex)
		{
			return null;
		}
	}
	
	/**
	 * This method will handle retrieving any information about a specific user.
	 * 
	 * @param username
	 * @return User (if found)
	 */
	@RequestMapping(value = "/get-user-info", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public User getUserInformation(String username)
	{
		User user = new User();
		try
		{
			user = userDao.findByUsername(username);
			return user;
		}
		catch(Exception ex)
		{
			return null;
		}
	}
	
	/**
	 * This method will handle Creating new user accounts. It will salt and hash the user's password for 
	 * added security.
	 * 
	 * @param username
	 * @param password
	 * @param email
	 * @param matricNo
	 * @param firstName
	 * @param lastName
	 * @param accountType
	 * @return True/False 
	 */
	@RequestMapping(value = "/create-user", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public boolean createUser(String username, String password, String email, String matricNo, String firstName, String lastName, String accountType){
		try{
		    
			//check to see if username has been taken!!!
			SecureRandom random = new SecureRandom();
	        byte[] salt = new byte[32];
	        random.nextBytes(salt);
	        String encodedPassword = null;   
			String hexSalt = null;
			encodedPassword = PaswordConvertorSHA256.getHash(password, salt);
            hexSalt = PaswordConvertorSHA256.convertToHex(salt);
			User user = new User(username, encodedPassword, hexSalt, email, matricNo, firstName, lastName, accountType);
			userDao.save(user);
			return true;
		}
		catch(NoSuchAlgorithmException et) {
			System.out.println("Can't hash the password");
			return false;    
        }
		catch (InvalidKeySpecException ex) {
			return false;
        }
		catch(Exception ex){
			return false;
		}
	}
	
	/**
	 * This method will handle updating any details about the users account. 
	 * 
	 * @param username
	 * @param email
	 * @param accountType
	 * @param firstName
	 * @param lastName
	 * @return True/False 
	 */
	@RequestMapping(value = "/update-user", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public boolean updateUserDetails(String username, String email, String accountType, String firstName, String lastName, String matricNo){
		try{
			User user = new User();
			user = userDao.findByUsername(username);
			user.setEmail(email);
			user.setAccountType(accountType);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setMatricNo(matricNo);
			userDao.save(user);
			return true;
		}
		catch(Exception ex){
			return false;
		}
	}
	
	/**
	 * This method will check if a username is available
	 * 
	 * @param username
	 * @return True/False
	 */
	@RequestMapping(value = "/check-username-available", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public boolean isUsernameAvailable(String username){
		try{
			User user = userDao.findByUsername(username);
			if(user != null){
				return false;
			}else{
				return true;
			}
		}
		catch(Exception ex){
			return false;
		}
	}
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoomBookingDao roomBookingDao;
}
