package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class CancelBookingMethodTest {
	private final String hotelcode = "ABC1234";
	private final String hotelname = "IBIZA";
	private final String roomnumber = "1234";
	private final LocalDate arrival = new LocalDate(2017, 12, 19);
	private final LocalDate departure = new LocalDate(2017, 12, 20);
	private Hotel hotel;
	private Room room;
	private String reference;

	@Before
	public void setUp() {
		this.hotel = new Hotel(this.hotelcode, this.hotelname);
		this.room = new Room(this.hotel, this.roomnumber, Type.SINGLE);
		this.reference = this.hotel.reserveRoom(Type.SINGLE, this.arrival, this.departure);
	}

	@Test(expected = HotelException.class)
	public void cancelBookingNullReference() {
		this.hotel.cancelBooking(null);
	}

	@Test(expected = HotelException.class)
	public void cancelBookingBlankReference() {
		this.hotel.cancelBooking("   ");
	}

	@Test(expected = HotelException.class)
	public void cancelBookingTReference() {
		this.hotel.cancelBooking("\t");
	}

	@Test(expected = HotelException.class)
	public void cancelBookingFalseReference() {
		this.hotel.cancelBooking("blablabla");
	}

	@Test
	public void cancelBooking() {
		this.hotel.cancelBooking(this.reference);
		Set<Room> _rooms = this.hotel.getRooms();
		for (Room room : _rooms){
			if (room.getBooking(this.reference) != null){
				Assert.assertEquals(this.reference + "cancelled", room.getBooking(reference).getCancellationReference());
			}
		}
	}

	//Cancelar duas vezes
	@Test(expected = HotelException.class)
	public void recancelBooking() {
		this.hotel.cancelBooking(this.reference);
		this.hotel.cancelBooking(this.reference);
	}		

	//Cancelar depois da data
	@Test(expected = HotelException.class)
	public void cancelPreviousBooking() {
		LocalDate newArrival = new LocalDate(2016, 12, 19);
		LocalDate newDeparture = new LocalDate(2016, 12, 20);
		String newReference = this.hotel.reserveRoom(Type.SINGLE, newArrival, newDeparture);
		this.hotel.cancelBooking(newReference);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}


