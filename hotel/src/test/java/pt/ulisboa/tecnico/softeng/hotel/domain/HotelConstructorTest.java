package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelConstructorTest {

	@Before
	public void setUp() {

	}

	@Test
	public void success() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		Assert.assertEquals("Londres", hotel.getName());
		Assert.assertTrue(hotel.getCode().length() == Hotel.CODE_SIZE);
		Assert.assertEquals(0, hotel.getNumberOfRooms());
		Assert.assertEquals(1, Hotel.hotels.size());
	}
	
	@Test(expected = HotelException.class)
	public void nullString(){
		Hotel hotel = new Hotel(null, null);
	}
	
	@Test(expected = HotelException.class)
	public void unique(){
		Hotel hotel = new Hotel("XPTO123", "Londres");
		Hotel hotel2 = new Hotel("XPTO123", "Lds");
	}
	
	@Test(expected = HotelException.class)
	public void sevenChars(){
		Hotel hotel = new Hotel("XP11", "Londres");
	}
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
	
}
