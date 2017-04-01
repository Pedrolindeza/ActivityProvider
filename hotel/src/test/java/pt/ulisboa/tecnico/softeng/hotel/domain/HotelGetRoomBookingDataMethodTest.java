package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.Assert;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelGetRoomBookingDataMethodTest {
	
	Hotel hotel;
	Booking booking;
	Room room;
	
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);
	private static final String HOTEL_NAME = "Londres";
	private static final String HOTEL_CODE = "XPTO123";
	private static boolean firstTime = false;
	
	@Before
	public  void setUp(){
		if(!firstTime){
		this.hotel = new Hotel(HOTEL_CODE, HOTEL_NAME);
		this.room = new Room (hotel,"20",Type.DOUBLE);
		this.booking = room.reserve(Type.DOUBLE, arrival, departure);
		firstTime=true;
		}
		else{
			return;
		}
	}
	@Test
	public void success(){
		
		RoomBookingData rbd = Hotel.getRoomBookingData(booking.getReference());
		Assert.assertEquals(rbd.getReference(),booking.getReference());
		Assert.assertEquals(rbd.getArrival(),booking.getArrival());
		Assert.assertEquals(rbd.getDeparture(),booking.getDeparture());
		Assert.assertEquals(rbd.getHotelCode(),hotel.getCode());
		Assert.assertEquals(rbd.getHotelName(),hotel.getName());
		Assert.assertEquals(rbd.getRoomNumber(),room.getNumber());
		Assert.assertEquals(rbd.getRoomType().toString(),room.getType().toString());
	}
	@Test(expected = HotelException.class)
	public void nullReference(){
		Hotel.getRoomBookingData(null);
	}
	@Test(expected = HotelException.class)
	public void blankReference(){
		Hotel.getRoomBookingData("");
	}
	@Test(expected = HotelException.class)
	public void whiteSpaceReference(){
		Hotel.getRoomBookingData("           \n");
	}
	@Test(expected = HotelException.class)
	public void wrongReference(){
		Hotel.getRoomBookingData("ref");
	}
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
	
}
