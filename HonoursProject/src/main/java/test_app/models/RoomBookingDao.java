package test_app.models;

import java.util.LinkedList;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface RoomBookingDao extends CrudRepository<RoomBooking, Long> {
	public RoomBooking findById(long id);
	
	public RoomBooking findByTitle(String title);
	
	public LinkedList<RoomBooking> findAllByRoomCodeAndDateOfBooking(String roomCode, String dateOfBooking);
	
	public LinkedList<RoomBooking> findAllByCreatorOrderByDateOfBookingAscStartTimeAsc(String creator);
	
	public LinkedList<RoomBooking> findAllByOrderByDateOfBookingAsc();
	
	public LinkedList<RoomBooking> findByRoomCodeOrderByDateOfBookingAsc(String roomCode);
	
	public LinkedList<RoomBooking> findAllByDateOfBooking(String dateOfBooking);
	
}
