package test_app.controllers;

import test_app.models.EventSignUp;
import test_app.models.EventSignUpDao;
import test_app.models.Room;
import test_app.models.RoomBooking;
import test_app.models.RoomBookingDao;
import test_app.models.RoomDao;

import org.springframework.http.HttpStatus;

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
 * This Controller will handle URL requests for Room Bookings. 
 * 
 * @author Dreads
 */
@Controller
public class RoomBookingController {
	/**
	 * This method will search for a RoomBooking by a specific ID.
	 * 
	 * @param id
	 * @return the RoomBooking (if found)
	 */
	@RequestMapping(value = "/get-roombooking-info-id", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public RoomBooking getRoomBookingInfoById(long id){
		try{
			RoomBooking roomBooking = new RoomBooking();
			roomBooking = roomBookingDao.findById(id);
			return roomBooking;
		}
		catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * This method will search for a RoomBooking by a specific Title.
	 * 
	 * @param title
	 * @return RoomBooking (if found) 
	 */
	@RequestMapping(value = "/get-roombooking-info-title", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public RoomBooking getRoomBookingInfoByTitle(String title){
		try{
			RoomBooking roomBooking = new RoomBooking();
			roomBooking = roomBookingDao.findByTitle(title);
			return roomBooking;
		}
		catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * This method will return all RoomBookings in the system. 
	 * 
	 * @return LinkedList containing all of the RoomBookings 
	 */
	@RequestMapping(value = "/get-all-bookings", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public LinkedList<RoomBooking> getAllBookings(){
		try{
			LinkedList<RoomBooking> roomBookingList = roomBookingDao.findAllByOrderByDateOfBookingAsc();
			return roomBookingList;
		}
		catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * This method will get all the room bookings for a single room.
	 * 
	 * @param roomCode
	 * @return roomBookingList (LinkedList of a rooms bookings)
	 */
	@RequestMapping(value = "/get-all-roombookings", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public LinkedList<RoomBooking> getAllRoomBookings(String roomCode){
		try{
			LinkedList<RoomBooking> roomBookingList = roomBookingDao.findByRoomCodeOrderByDateOfBookingAsc(roomCode);
			return roomBookingList;
		}
		catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * This method will be called when creating a RoomBooking.
	 * @param roomCode
	 * @param dateOfBooking
	 * @param startTime
	 * @param endTime
	 * @param title
	 * @param description
	 * @return True/False
	 */
	@RequestMapping(value = "/create-roombooking", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public boolean createRoomBooking(String roomCode, String dateOfBooking, String startTime, String endTime, String title, String description, String creator){
		try{
			RoomBooking roomBooking = new RoomBooking(roomCode, dateOfBooking, startTime, endTime, title, description, creator);
			LinkedList<RoomBooking> roomBookingList = roomBookingDao.findAllByRoomCodeAndDateOfBooking(roomCode, dateOfBooking);
			for(RoomBooking i : roomBookingList){
				String OBST = i.getStartTime();
				String OBET = i.getEndTime();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				Date obst = sdf.parse(OBST);
				Date nbst = sdf.parse(startTime);
				Date obet = sdf.parse(OBET);
				Date nbet = sdf.parse(endTime);
				if(nbst.equals(obst) && nbet.equals(obet)){
					return false;
				}
				if(nbst.before(obst) && nbet.after(obet)){
					return false;
				}
				if(nbst.before(obet) && nbst.after(obst)){
					return false;
				}
				if(nbet.after(obst) && nbet.before(obet)){
					return false;
				}
				if(nbst.equals(obst) && nbet.after(obet)){
					return false;
				}
				if(nbst.before(obst) && nbet.equals(obet)){
					return false;
				}
			}
			roomBookingDao.save(roomBooking);
			return true;
		}
		catch(Exception ex){
			return false;
		}
	}
	
	/**
	 * This method will return a user's list of bookings
	 *  
	 * @param username
	 * @return bookingList (LinkedList of room bookings)
	 */
	@RequestMapping(value = "/get-user-room-booking", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public LinkedList<RoomBooking> getUserBookingList(String username){
		try{
			LinkedList<RoomBooking> bookingList = roomBookingDao.findAllByCreatorOrderByDateOfBookingAscStartTimeAsc(username);
			return bookingList;

		}catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * This method will delete a user's room booking
	 * 
	 * @param username
	 * @param eventId
	 * @return True/False
	 */
	@RequestMapping(value = "/delete-user-room-booking", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public boolean deleteUserBooking(String username, String eventId){
		try{
			RoomBooking rb = roomBookingDao.findById(Long.parseLong(eventId));
			if(rb.getCreator().equals(username)){
				LinkedList<EventSignUp> eventSignUpsList = eventSignUpDao.findAllByEventId(Long.parseLong(eventId));
				if(eventSignUpsList.isEmpty()){
				}else{
					for(EventSignUp esu : eventSignUpsList){
						eventSignUpDao.delete(esu);
					}
				}
				roomBookingDao.delete(rb);
				return true;
			}else{
				return false;
			}
		}catch(Exception ex){
			return false;
		}
	}
	
	/**
	 * This method will search for an available room at a specific date and time
	 * 
	 * @param dateOfBooking
	 * @param startTime
	 * @param endTime
	 * @return roomList (LinkedList of available rooms)
	 */
	@RequestMapping(value = "/search-free-room", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public LinkedList<Room> searchForFreeTime(String dateOfBooking, String startTime, String endTime){
		try{
			LinkedList<Room> roomList = roomDao.findAll();
			LinkedList<Room> availableRoomList = new LinkedList<Room>();
			for(Room rm : roomList){
				String roomCode = rm.getIdRoom();
				LinkedList<RoomBooking> bookingList = roomBookingDao.findAllByRoomCodeAndDateOfBooking(roomCode, dateOfBooking);
				boolean temp = true;
				if(bookingList.isEmpty()){
					availableRoomList.add(rm);
				}else{
					for(RoomBooking rb : bookingList){
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
						String OBST = rb.getStartTime();
						String OBET = rb.getEndTime();
						Date obst = sdf.parse(OBST);
						Date nbst = sdf.parse(startTime);
						Date obet = sdf.parse(OBET);
						Date nbet = sdf.parse(endTime);
						if(nbst.equals(obst) && nbet.equals(obet)){
							temp = false;
						}
						if(nbst.before(obst) && nbet.after(obet)){
							temp = false;
						}
						if(nbst.before(obet) && nbst.after(obst)){
							temp = false;
						}
						if(nbet.after(obst) && nbet.before(obet)){
							temp = false;
						}
						if(nbst.equals(obst) && nbet.after(obet)){
							temp = false;
						}
						if(nbst.before(obst) && nbet.equals(obet)){
							temp = false;
						}
					}
					if(temp == true){
						availableRoomList.add(rm);
					}
				}
			}
			return availableRoomList;
		}
		catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * This method will search for a room with a whiteboard at a specific date and time
	 * 
	 * @param whiteboard
	 * @param dateOfBooking
	 * @param startTime
	 * @param endTime
	 * @return roomList (LinkedList of Available Rooms)
	 */
	@RequestMapping(value = "/advanced-search-whiteboard", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public LinkedList<Room> advancedSearchWhiteboard(String whiteboard, String dateOfBooking, String startTime, String endTime){
		try{
			LinkedList<Room> roomList = searchForFreeTime(dateOfBooking, startTime, endTime);
			//LinkedList<Room> roomList = roomDao.findAll();
			LinkedList<Room> availableRooms = new LinkedList<Room>();
			for(Room rm : roomList){
				String roomWhiteboard = rm.getWhiteboard();
				if(whiteboard.equals(roomWhiteboard)){
					availableRooms.add(rm);
				}
			}
			return availableRooms;
		}catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * This method will search for a room with a projector at a specific time and date.
	 * 
	 * @param projector
	 * @param dateOfBooking
	 * @param startTime
	 * @param endTime
	 * @return roomList (LinkedList of Available Rooms)
	 */
	@RequestMapping(value = "/advanced-search-projector", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public LinkedList<Room> advancedSearchProjector(String projector, String dateOfBooking, String startTime, String endTime){
		try{
			LinkedList<Room> roomList = searchForFreeTime(dateOfBooking, startTime, endTime);
			LinkedList<Room> availableRooms = new LinkedList<Room>();
			for(Room rm : roomList){	
				String roomProjector = rm.getProjector();
				if(projector.equals(roomProjector)){
					availableRooms.add(rm);
				}
			}
			return availableRooms;
		}catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * This method will search for a room with a whiteboard and a projector at a specific time and date.
	 * 
	 * @param whiteboard
	 * @param projector
	 * @param dateOfBooking
	 * @param startTime
	 * @param endTime
	 * @return roomList (LinkedList of Available Rooms)
	 */
	@RequestMapping(value = "/advanced-search-whiteboard-projector", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public LinkedList<Room> advancedSearchWhiteboardAndProjector(String whiteboard, String projector, String dateOfBooking, String startTime, String endTime){
		try{
			LinkedList<Room> roomList = searchForFreeTime(dateOfBooking, startTime, endTime);
			LinkedList<Room> availableRooms = new LinkedList<Room>();
			for(Room rm : roomList){
				String roomWhiteboard = rm.getWhiteboard();
				if(roomWhiteboard.equals(whiteboard)){
					String roomProjector = rm.getProjector();
					if(projector.equals(roomProjector)){
						availableRooms.add(rm);
					}
				}
			}
			return availableRooms;
		}catch(Exception ex){
			return null;
		}
	}
	/**
	 * This method will search for a room with conferencing equipment at a specific time and date. 
	 * 
	 * @param confEquipment
	 * @param dateOfBooking
	 * @param startTime
	 * @param endTime
	 * @return roomList (LinkedList of Available Rooms)
	 */
	@RequestMapping(value = "/advanced-search-conference-equipment", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public LinkedList<Room> advancedSearch(String confEquipment, String dateOfBooking, String startTime, String endTime){
		try{
			LinkedList<Room> roomList = searchForFreeTime(dateOfBooking, startTime, endTime);
			LinkedList<Room> availableRooms = new LinkedList<Room>();
			for(Room rm : roomList){
				String roomConfEquipment = rm.getConfEquipment();
				if(roomConfEquipment.equals(confEquipment)){
					availableRooms.add(rm);
				}
			}
			return availableRooms;
		}catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * This method will search for a room with a whiteboard and conferencing equipment at a specific date and time.
	 * 
	 * @param whiteboard
	 * @param confEquipment
	 * @param dateOfBooking
	 * @param startTime
	 * @param endTime
	 * @return roomList (LinkedList of Available Room)
	 */
	@RequestMapping(value = "/advanced-search-whiteboard-conference-equipment", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public LinkedList<Room> advancedSearchWhiteboardAndConfEquipment(String whiteboard, String confEquipment, String dateOfBooking, String startTime, String endTime){
		try{
			LinkedList<Room> roomList = searchForFreeTime(dateOfBooking, startTime, endTime);
			LinkedList<Room> availableRooms = new LinkedList<Room>();
			for(Room rm : roomList){
				String roomWhiteboard = rm.getWhiteboard();
				if(roomWhiteboard.equals(whiteboard)){
					String roomConfEquipment = rm.getConfEquipment();
					if(confEquipment.equals(roomConfEquipment)){
						availableRooms.add(rm);
					}
				}
			}
			return availableRooms;
		}catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * This method will search for a room with a projector and conferencing equipment at a specific date and time.
	 * 
	 * @param projector
	 * @param confEquipment
	 * @param dateOfBooking
	 * @param startTime
	 * @param endTime
	 * @return roomList (LinkedList of Available Room)
	 */
	@RequestMapping(value = "/advanced-search-projector-conference-equipment", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public LinkedList<Room> advancedSearchProjectorAndConfEquipment(String projector, String confEquipment, String dateOfBooking, String startTime, String endTime){
		try{
			LinkedList<Room> roomList = searchForFreeTime(dateOfBooking, startTime, endTime);
			LinkedList<Room> availableRooms = new LinkedList<Room>();
			for(Room rm : roomList){
				String roomProjector = rm.getProjector();
				if(projector.equals(roomProjector)){
					String roomConfEquipment = rm.getConfEquipment();
					if(confEquipment.equals(roomConfEquipment)){
						availableRooms.add(rm);
					}
				}
			}
			return availableRooms;
		}catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * This method will search for a room with a whiteboard, projector and conferencing equipment at a specific date and time.
	 * 
	 * @param whiteboard
	 * @param projector
	 * @param confEquipment
	 * @param dateOfBooking
	 * @param startTime
	 * @param endTime
	 * @return roomList (LinkedList of Available Room)
	 */
	@RequestMapping(value = "/advanced-search-whiteboard-projector-conference-equipment", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus (HttpStatus.OK)
	public LinkedList<Room> advancedSearchWhiteboardAndProjectorAndConfEquipment(String whiteboard, String projector, String confEquipment, String dateOfBooking, String startTime, String endTime){
		try{
			LinkedList<Room> roomList = searchForFreeTime(dateOfBooking, startTime, endTime);
			LinkedList<Room> availableRooms = new LinkedList<Room>();
			for(Room rm : roomList){
				String roomWhiteboard = rm.getWhiteboard();
				if(roomWhiteboard.equals(whiteboard)){
					String roomProjector = rm.getProjector();
					if(projector.equals(roomProjector)){
						String roomConfEquipment = rm.getConfEquipment();
						if(confEquipment.equals(roomConfEquipment)){
							availableRooms.add(rm);
						}
					}
				}
			}
			return availableRooms;
		}catch(Exception ex){
			return null;
		}
	}
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private EventSignUpDao eventSignUpDao;
	
	@Autowired
	private RoomBookingDao roomBookingDao;
}
