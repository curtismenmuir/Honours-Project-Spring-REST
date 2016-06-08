package test_app.models;

public class SignUp {

	private long id;
	private String dateOfBooking;
	private String title;
	private String description;
	private String startTime;
	private String endTime;
	private String creator;
	
	public SignUp(){ }

	public SignUp(long id, String dateOfBooking, String title, String description, String startTime, String endTime, String creator){
		this.id = id;
		this.dateOfBooking = dateOfBooking;
		this.title = title;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.creator = creator;
	}
	public long getId(){
		return this.id;
	}
	public void setId(long id){
		this.id = id;
	}
	public String getDateOfBooking(){
		return this.dateOfBooking;
	}
	public void setDateOfBooking(String dateOfBooking){
		this.dateOfBooking = dateOfBooking;
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
	public String getCreator(){
		return this.creator;
	}
	public void setCreator(String creator){
		this.creator = creator;
	}
}
