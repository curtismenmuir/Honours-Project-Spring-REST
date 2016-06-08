package test_app.controllers;

import test_app.models.EventSignUp;
import test_app.models.EventSignUpDao;
import test_app.models.RoomBooking;
import test_app.models.RoomBookingDao;
import test_app.models.SignUp;

import org.springframework.http.HttpStatus;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This Controller will handle url requests for Event Sign-ups.
 * 
 * @author Dreads
 */
@Controller
public class EventSignUpController {
	
	/**
	 * This method will return whether a user has signed up for an event.
	 * 
	 * @param eventId
	 * @param username
	 * @return True or False
	 */
	@RequestMapping(value = "/user-signup", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public boolean isUserSignedUp(long eventId, String username){
		try{
			LinkedList<EventSignUp> eventSignUpList = eventSignUpDao.findAllByEventId(eventId);
			for(EventSignUp i: eventSignUpList){
				if(i.getUsername().equals(username)){
					return true;
				}
			}
			return false;
		}
		catch(Exception ex){
			return false;
		}
	}
	/**
	 * This method will allow users to sign up for an event. It will return true 
	 * or false depending on if they have successfully signed up for the 
	 * event. 
	 * 
	 * @param eventId
	 * @param username
	 * @return True/False
	 */
	@RequestMapping(value = "/create-signup", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public boolean createSignUp(long eventId, String username){
		try{
			EventSignUp eventSignUp = new EventSignUp(eventId, username);
			eventSignUpDao.save(eventSignUp);
			return true;
		}
		catch(Exception ex){
			return false;
		}
	}
	
	/**
	 * This method will Cancel a signup for an event. It will search for a 
	 * specific user which has signed up for a specific event. If they are 
	 * found then they will be removed from the event signed up list. 
	 * 
	 * @param username
	 * @param eventId
	 * @return true/false
	 */
	@RequestMapping(value = "/cancel-signup", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public boolean cancelSignUp(long eventId, String username){
		try{
			EventSignUp eventSignUp = eventSignUpDao.findByUsernameAndEventId(username, eventId);
			eventSignUpDao.delete(eventSignUp);
			return true;
		}
		catch(Exception ex){
			return false;
		}
	}
	
	/**
	 * This method will return all signups for a specific event.
	 * 
	 * @param eventId
	 * @return LinkedList of Event Sign-Ups. 
	 */
	@RequestMapping(value = "/get-all-signups", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public LinkedList<EventSignUp> getAllSignUps(long eventId){
		try{
			LinkedList<EventSignUp> eventSignUpList = eventSignUpDao.findAllByEventId(eventId);
			return eventSignUpList;
		}
		catch(Exception ex){
			return null;
		}
	}
	/**
	 * This method  will return all events that a specific user has signed up for.
	 * 
	 * @param username
	 * @return LinkedList of Events (if found)
	 */
	@RequestMapping(value = "/get-all-user-events", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public LinkedList<SignUp> getUserEvents(String username){
		try{
			LinkedList<EventSignUp> eventList = eventSignUpDao.findAllByUsername(username);
			LinkedList<SignUp> signUpList = new LinkedList<SignUp>();
			for(EventSignUp esu : eventList){
				RoomBooking su = roomBookingDao.findById(esu.getEventId());
				long id = su.getId();
				String dateOfBooking = su.getDateOfBooking();
				String title = su.getTitle();
				String description = su.getDescription();
				String startTime = su.getStartTime();
				String endTime = su.getEndTime();
				String creator = su.getCreator();
				SignUp signUp = new SignUp(id, dateOfBooking, title, description, startTime, endTime, creator);
				signUpList.add(signUp);
			}
			return signUpList;
		}
		catch(Exception ex){
			return null;
		}
	}
	
	@Autowired 
	private RoomBookingDao roomBookingDao;
	
	@Autowired
	private EventSignUpDao eventSignUpDao;
}
