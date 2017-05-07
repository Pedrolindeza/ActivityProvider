package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

public class BookingData {
	private Room room;
	@DateTimeFormat (pattern= "yyyy-MM-dd")
	private LocalDate arrival;
	@DateTimeFormat (pattern= "yyyy-MM-dd")
	private LocalDate departure;
	private String reference;
	
	
	public BookingData(){
	}
	
	public BookingData(Booking booking) {
		this.room = booking.getRoom();
		this.arrival = booking.getArrival();
		this.departure = booking.getDeparture();
		this.reference = booking.getReference();

	}
	
	public String getReference(){
		return this.reference;
	}
	
	public Room getRoom(){
		return this.room;
	}
	
	public LocalDate getArrival(){
		return this.arrival;
	}
	
	public LocalDate getDeparture(){
		return this.departure;
	}
	
	public void setReference(String reference){
		this.reference = reference;
	}
	
	public void setRoom(Room room){
		this.room = room;
	}
	
	public void setArrival(LocalDate arrival){
		this.arrival = arrival;
	}
	
	public void setDeparture(LocalDate departure){
		this.departure = departure;
	}
}
