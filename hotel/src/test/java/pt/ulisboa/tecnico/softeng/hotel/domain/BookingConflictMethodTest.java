package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BookingConflictMethodTest {
	Booking booking;

	@Before
	public void setUp() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 24);
		this.booking = new Booking(hotel, arrival, departure);
	}
/*
	18	19	20	21	22	23	24	25
OG 		X  	X 	X 	X 	X 	X
C1			X 	X 	X
C2	X 	X 	X
C3						X 	X 	X
*/
	@Test
	public void noConflictBefore() {
		LocalDate arrival = new LocalDate(2016, 12, 16);
		LocalDate departure = new LocalDate(2016, 12, 19);

		Assert.assertFalse(this.booking.conflict(arrival, departure));
	}

	@Test
	public void noConflictAfter() {
		LocalDate arrival = new LocalDate(2016, 12, 24);
		LocalDate departure = new LocalDate(2016, 12, 30);

		Assert.assertFalse(this.booking.conflict(arrival, departure));
	}

	@Test
	public void noConflict1(){
		LocalDate arrival = new LocalDate(2016, 12, 20);
		LocalDate departure = new LocalDate(2016, 12, 22);

		Assert.assertTrue(this.booking.conflict(arrival, departure));	
	}

	@Test
	public void noConflict2(){
		LocalDate arrival = new LocalDate(2016, 12, 18);
		LocalDate departure = new LocalDate(2016, 12, 20);

		Assert.assertTrue(this.booking.conflict(arrival, departure));	
	}

	@Test
	public void noConflict3(){
		LocalDate arrival = new LocalDate(2016, 12, 23);
		LocalDate departure = new LocalDate(2016, 12, 25);

		Assert.assertTrue(this.booking.conflict(arrival, departure));	
	}
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
