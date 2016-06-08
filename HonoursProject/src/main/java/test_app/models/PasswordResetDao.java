package test_app.models;


import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface PasswordResetDao extends CrudRepository<PasswordReset, Long>{
	public PasswordReset findByToken(String token);
	
	public PasswordReset findByUsername(String username);
}
