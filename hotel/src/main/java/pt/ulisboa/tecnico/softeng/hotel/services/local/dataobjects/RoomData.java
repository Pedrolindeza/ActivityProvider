package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class RoomData {

	
	private Hotel hotel;
	private String number;
	private  Type type;
	private List<BookingData> bookings = new ArrayList<>();
	
	public RoomData(){
		
	}
	
	public RoomData(Room room) {
		this.hotel=room.getHotel();
		this.number=room.getNumber();
		this.type=room.getType();
		for(Booking booking: room.getBookingSet()){
			this.bookings.add(new BookingData(booking));
		}
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
	
	public List<BookingData> getBookings() {
		return this.bookings;
	}

	public void setBookings(List<BookingData> bookings) {
		this.bookings = bookings;
	}
}
