package test_app.models;

import java.util.LinkedList;

import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface BuildingDao extends CrudRepository<Building, Long>{
	public LinkedList<Building> findAll();
}
