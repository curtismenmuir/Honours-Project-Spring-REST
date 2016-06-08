package test_app.models;

import java.util.LinkedList;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface RoomDao extends CrudRepository<Room, Long> {
	public Room findByIdRoom(String idRoom);
	
	public Room findByNameRoom(String nameRoom);
	
	public LinkedList<Room> findAllByBuildingNameAndFloorNo(String buildingName, int floorNo);
	
	public LinkedList<Room> findAll();
}
