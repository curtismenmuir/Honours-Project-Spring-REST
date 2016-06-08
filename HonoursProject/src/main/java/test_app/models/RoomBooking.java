package test_app.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "room_booking")
public class RoomBooking {

	// An auto-generated id (unique for each room booking in the db)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	private String roomCode;
	
	@NotNull
	private String dateOfBooking;
	
	@NotNull 
	private String startTime;
	
	@NotNull
	private String endTime;
	
	@NotNull
	private String title;
	
	private String description;
	
	@NotNull
	private String creator;
	
	public RoomBooking(){}
	
	public RoomBooking(long id){
		this.id = id;
	}
	
	public RoomBooking(String roomCode, String dateOfBooking, String startTime, String endTime, String title, String description, String creator){
		this.roomCode = roomCode;
		this.dateOfBooking = dateOfBooking;
		this.startTime = startTime;
		this.endTime = endTime;
		this.title = title;
		this.description = description;
		this.creator = creator;
	}
	
	public long getId(){
		return this.id;
	}
	public void setId(long id){
		this.id = id;
	}
	public String getRoomCode(){
		return this.roomCode;
	}
	public void setRoomCode(String roomCode){
		this.roomCode = roomCode;
	}
	public String getDateOfBooking(){
		return this.dateOfBooking;
	}
	public void setDateOfBooking(String dateOfBooking){
		this.dateOfBooking = dateOfBooking;
	}
	
	public String getStartTime(){
		return this.startTime;
	}
	public void setStartTime(String startTime){
		this.startTime = startTime;
	}
	
	public String getEndTime(){
		return this.endTime;
	}
	public void setEndTime(String endTime){
		this.endTime = endTime;
	}
	
	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public String getDescription(){
		return this.description;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public String getCreator(){
		return this.creator;
	}
	public void setCreator(String creator){
		this.creator = creator;
	}
}
