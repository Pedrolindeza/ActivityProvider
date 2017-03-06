package pt.ulisboa.tecnico.softeng.hotel.domain;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import org.joda.time.LocalDate;

public class Booking {
	private static int counter = 0;

	private final String reference;
	private final LocalDate arrival;
	private final LocalDate departure;

	Booking(Hotel hotel, LocalDate arrival, LocalDate departure) {
		try{
			departureAfterArrival(arrival,departure);
		} catch(HotelException e){
			System.err.println("HotelException: " + e.getMessage());
		}
		this.reference = hotel.getCode() + Integer.toString(++Booking.counter);
		this.arrival = arrival;
		this.departure = departure;
	}

	public void departureAfterArrival(LocalDate arrival, LocalDate departure){
		if (arrival.isAfter(departure)){
			throw new HotelException("Arrival and Departure dates incompatible");
		}
	}

	public String getReference() {
		return this.reference;
	}

	LocalDate getArrival() {
		return this.arrival;
	}

	LocalDate getDeparture() {
		return this.departure;
	}

	boolean conflict(LocalDate arrival, LocalDate departure) {
		if (arrival.isAfter(this.arrival) && arrival.isBefore(this.departure)) {
			return true;
		}
		if (departure.isAfter(this.arrival) && departure.isBefore(this.departure)) {
			return true;
		}
		if (arrival.isBefore(this.arrival) && departure.isAfter(this.departure)) {
			return true;
		}
		return false;
	}
}
