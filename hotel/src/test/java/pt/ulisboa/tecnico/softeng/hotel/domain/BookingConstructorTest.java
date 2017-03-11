package pt.ulisboa.tecnico.softeng.hotel.domain;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BookingConstructorTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void success() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		Booking booking = new Booking(hotel, arrival, departure);

		Assert.assertTrue(booking.getReference().startsWith(hotel.getCode()));
		Assert.assertTrue(booking.getReference().length() > Hotel.CODE_SIZE);
		Assert.assertEquals(arrival, booking.getArrival());
		Assert.assertEquals(departure, booking.getDeparture());
	}
/*Hotel - Task 3*/
	@Test
	public void switchedDates() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		LocalDate arrival = new LocalDate(2016, 12, 21);
		LocalDate departure = new LocalDate(2016, 12, 19);

		exception.expect(HotelException.class);
		Booking booking = new Booking(hotel, arrival, departure);	
	}
	@Test
	public void nullDates() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		LocalDate arrival = null;
		LocalDate departure = null;

		exception.expect(NullPointerException.class);
		Booking booking = new Booking(hotel, arrival, departure);	
	}
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
