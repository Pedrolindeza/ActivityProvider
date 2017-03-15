package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Room {
	private static Logger logger = LoggerFactory.getLogger(Room.class);

	public static enum Type {
		SINGLE, DOUBLE
	}

	private final Hotel hotel;
	private final String number;
	private final Type type;
	private final Set<Booking> bookings = new HashSet<>();

	public Room(Hotel hotel, String number, Type type) {
		check(number,hotel,type);
		this.hotel = hotel;
		this.number = number;
		this.type = type;

		this.hotel.addRoom(this);
	}
	public void check(String number, Hotel hotel, Type type){
		if(number==null || hotel==null || type==null){
			throw new HotelException("Argumentos null \nclasse: Room \nmétodo: check\n");
		}
		try {
		    int inteiro = Integer.parseInt(number);
		} catch (NumberFormatException e) {
			throw new HotelException("Not a number!\n");
		}
		
	}
	Hotel getHotel() {
		return this.hotel;
	}

	String getNumber() {
		return this.number;
	}

	Type getType() {
		return this.type;
	}

	boolean isFree(Type type, LocalDate arrival, LocalDate departure) {
		if (!type.equals(this.type)) {
			return false;
		}

		for (Booking booking : this.bookings) {
			if (booking.conflict(arrival, departure)) {
				return false;
			}
		}

		return true;
	}

	public Booking reserve(Type type, LocalDate arrival, LocalDate departure) {
		if (type==null || arrival==null || departure==null || !isFree(type, arrival, departure)) {
			throw new HotelException("Argumentos null \nclasse: Room \nmétodo: reserve\n");
		}

		Booking booking = new Booking(this.hotel, arrival, departure);
		this.bookings.add(booking);

		return booking;
	}

}
