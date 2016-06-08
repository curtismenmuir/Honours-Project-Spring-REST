package test_app.models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "password_reset")
public class PasswordReset {

	// An auto-generated id (unique for each reset record in the db)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	private String token;
	
	@NotNull
	private String username;
	
	@NotNull
	private Timestamp dateTimeRequested;
	
	public PasswordReset(){ }
	
	public PasswordReset(String username){
		this.username = username;
	}
	public PasswordReset(String token, String username, Timestamp  dateTimeRequested){
		this.token = token;
		this.username = username;
		this.dateTimeRequested = dateTimeRequested;
	}
	public long getId(){
		return this.id;
	}
	public void setId(long id){
		this.id = id;
	}
	public String getToken(){
		return this.token;
	}
	public void setToken(String token){
		this.token = token;
	}
	public String getUsername(){
		return this.username;
	}
	public void setUsername(String username){
		this.username = username;
	}
	public Timestamp getDateTimeRequested(){
		return this.dateTimeRequested;
	}
	public void setDateTimeRequested(Timestamp dateTimeRequested){
		this.dateTimeRequested = dateTimeRequested;
	}
	
	
}
