package test_app.models;

import javax.validation.constraints.NotNull;

public class Picture {

	
	@NotNull
	private String username;
	
	private String imageString;

	public Picture(){ }
	
	public Picture(String imageString, String username){
		this.imageString = imageString;
		this.username = username;
	}
	
	public String getImageString(){
		return this.imageString;
	}
	public void setImageString(String imageString){
		this.imageString = imageString;
	}
	
	public String getUsername(){
		return this.username;
	}
	public void setUsername(String username){
		this.username = username;
	}
}
