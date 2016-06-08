package test_app.models;


import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface UserPictureDao extends CrudRepository<UserPicture, Long> {
	public UserPicture findByUsername(String username);
}
