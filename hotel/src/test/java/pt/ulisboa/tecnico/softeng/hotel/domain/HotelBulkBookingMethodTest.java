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

public class HotelBulkBookingMethodTest {
	public static final LocalDate arrival = new LocalDate(2016, 12, 19);
	public static final LocalDate departure = new LocalDate(2016, 12, 21);
	public static final String HOTEL_NAME = "Londres";
	public static final String HOTEL_CODE = "XPTO123";

	Hotel hotel;
	Set<String> newBookings = new HashSet<String>();
	String booking1, booking2;
	
	@Before
	public  void setUp(){
		this.hotel = new Hotel(HOTEL_CODE, HOTEL_NAME);
		Room room1 = new Room (this.hotel,"20",Type.SINGLE);
		Room room2 = new Room (this.hotel,"21",Type.SINGLE);
	}
		
	@Test
	public void success(){
		Set<String> newBookings2 = new HashSet<String>();
		newBookings2 = Hotel.bulkBooking(2, arrival, departure);
	}
	
	@Test(expected = HotelException.class)
	public void zeroNumber(){
		Hotel.bulkBooking(0, arrival, departure);
	}
	
	@Test(expected = HotelException.class)
	public void negativeNumber(){
		Hotel.bulkBooking(-1, arrival, departure);
	}
	
	@Test(expected = HotelException.class)
	public void higherNumber(){
		Hotel.bulkBooking(3, arrival, departure);
	}
	
	@Test(expected = HotelException.class)
	public void nullArrival(){
		Hotel.bulkBooking(2, null, departure);
	}
	
	@Test(expected = HotelException.class)
	public void nullDeparture(){
		Hotel.bulkBooking(2, arrival, null);
	}
	
	@Test(expected = HotelException.class)
	public void nullArrivalAndDeparture(){
		Hotel.bulkBooking(2, null, null);
	}
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
