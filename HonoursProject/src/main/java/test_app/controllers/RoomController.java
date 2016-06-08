package test_app.controllers;

import test_app.models.Room;
import test_app.models.RoomDao;

import org.springframework.http.HttpStatus;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This controller will handle URL requests about Rooms in the system. 
 * 
 * @author Dreads
 */
@Controller
public class RoomController {
	
	/**
	 * This method will search for a room by its roomID.
	 * 
	 * @param idRoom
	 * @return
	 */
	@RequestMapping(value = "/get-room-by-id", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Room getByIdRoom(String idRoom){
		
		Room room = new Room();
		try{
			room = roomDao.findByIdRoom(idRoom);
			return room;
		}
		catch(Exception ex){
			return null;
		}
		
	}
	
	
	@RequestMapping(value = "/get-room-by-name", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Room getByRoomName(String nameRoom){
		try{
			Room room = new Room();
			room = roomDao.findByNameRoom(nameRoom);
			return room;
		}catch(Exception ex){
			return null;
		}
	}
	@RequestMapping(value = "/get-rooms-by-building-floor", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public LinkedList<Room> getAllFloorRooms(String buildingName, int floorNo){
		try{
			LinkedList<Room> floorRoomList = roomDao.findAllByBuildingNameAndFloorNo(buildingName, floorNo);
			return floorRoomList;
		}
		catch(Exception ex){
			return null;
		}
	}
	@Autowired
	private RoomDao roomDao;
}
