package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel {
	public static Set<Hotel> hotels = new HashSet<>();

	static final int CODE_SIZE = 7;

	private final String code;
	private final String name;
	private final Set<Room> rooms = new HashSet<>();

	public Hotel(String code, String name) {
		checkArguments(code, name);

		this.code = code;
		this.name = name;
		Hotel.hotels.add(this);
	}

	private void checkArguments(String code, String name) {
		if (code == null || name == null || code.trim().length() == 0 || name.trim().length() == 0) {
			throw new HotelException();
		}

		if (code.length() != Hotel.CODE_SIZE) {
			throw new HotelException();
		}

		for (Hotel hotel : hotels) {
			if (hotel.getCode().equals(code)) {
				throw new HotelException();
			}
		}
	}

	public Room hasVacancy(Room.Type type, LocalDate arrival, LocalDate departure) {
		if (type == null || arrival == null || departure == null) {
			throw new HotelException();
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

	String getName() {
		return this.name;
	}

	void addRoom(Room room) {
		if (hasRoom(room.getNumber())) {
			throw new HotelException();
		}

		this.rooms.add(room);
	}

	int getNumberOfRooms() {
		return this.rooms.size();
	}

	public boolean hasRoom(String number) {
		for (Room room : this.rooms) {
			if (room.getNumber().equals(number)) {
				return true;
			}
		}
		return false;
	}

	public static String reserveRoom(Room.Type type, LocalDate arrival, LocalDate departure) {
		for (Hotel hotel : Hotel.hotels) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				return room.reserve(type, arrival, departure).getReference();
			}
		}
		throw new HotelException();
	}

	public static String cancelBooking(String roomConfirmation) {
		// TODO implement
		throw new HotelException();
	}
	
	public Set<Room> getRooms(){
		return rooms;
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		if(reference !=null && reference.trim().length()!=0){
			RoomBookingData rbd = new RoomBookingData();
			for(Hotel hotel : Hotel.hotels){
				Set<Room> hotelRooms = hotel.getRooms();
				for(Room room : hotelRooms){
					Booking booking = room.getBooking(reference);
					if(booking!=null){
						rbd.setReference(reference);
						rbd.setArrival(booking.getArrival());
						rbd.setDeparture(booking.getDeparture());
						rbd.setHotelCode(hotel.getCode());
						rbd.setHotelName(hotel.getName());
						rbd.setRoomNumber(room.getNumber());
						rbd.setRoomType(room.getType().toString());
						return rbd;
					}
				}
			}
		}
		throw new HotelException();
	}

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure) {
		Set<String> newBookings = new HashSet<String>();
		
		if(number!=0 && arrival!=null && departure!=null){
			try{
				int i=0;
				for(Hotel hotel : Hotel.hotels){
					Set<Room> hotelRooms = hotel.getRooms();
					for(Room room : hotelRooms){
						if(i<number){
							String bookingRef = room.reserve(room.getType(), arrival, departure).getReference();
							newBookings.add(bookingRef);
							i++;
						}
						else{
							return newBookings;
						}
					}
				}
				return newBookings;
			}
			catch(HotelException e){}
		}
		throw new HotelException();
	}

}
