package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel {
	public static Set<Hotel> hotels = new HashSet<>();

	static final int CODE_SIZE = 7;

	private final String code;
	private final String name;
	private final Set<Room> rooms = new HashSet<>();

	public Hotel(String code, String name) {
		checkCode(code);

		this.code = code;
		this.name = name;
		Hotel.hotels.add(this);
	}

	private void checkCode(String code) {
		if (code==null) {
			throw new HotelException();
		}
		if (code.length() != Hotel.CODE_SIZE) {
			throw new HotelException();
		}
		for(Hotel h : hotels){
			if(h.getCode().equals(code)){
				throw new HotelException();
			}
		}
	}

	public Room hasVacancy(Room.Type type, LocalDate arrival, LocalDate departure) {
		if (type == null || arrival == null || departure == null){
			throw new HotelException("Invalid arguments.");
		}
		for (Room room : this.rooms) {
			if (room.isFree(type, arrival, departure)) {
				return room;
			}
		}
		return null;
	}

	String getCode() {
		return this.code;
	}
	public boolean getRoom(String number){
		for(Room r : rooms){
			if(r.getNumber().equals(number)){
				return true;
			}
		}
		return false;
	}
	
	String getName() {
		return this.name;
	}
	public Set<Room> getRooms() {
		return this.rooms;
	}
	void addRoom(Room room) {

		if(getRoom(room.getNumber())){
			throw new HotelException();
		}
		this.rooms.add(room);
	}

	int getNumberOfRooms() {
		return this.rooms.size();
	}

	public static String reserveHotel(Room.Type type, LocalDate arrival, LocalDate departure) {
		for (Hotel hotel : Hotel.hotels) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				return room.reserve(type, arrival, departure).getReference();
			}
		}
		return null;
	}

}
