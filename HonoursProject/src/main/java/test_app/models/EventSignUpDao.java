package test_app.models;

import java.util.LinkedList;

import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface EventSignUpDao extends CrudRepository<EventSignUp, Long>{
	
	public LinkedList<EventSignUp> findAllByEventId(long eventId);
	
	public LinkedList<EventSignUp> findAllByUsername(String username);
	
	public EventSignUp findByUsernameAndEventId(String username, long eventId);
	
}
