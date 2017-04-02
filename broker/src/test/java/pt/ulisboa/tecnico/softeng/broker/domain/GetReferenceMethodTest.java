package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class GetReferenceMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private final String REFERENCE = "reference";
	private final String TYPE = "type";

	private BulkRoomBooking bulkRoomBooking;

	@Before
	public void setUp() {		
		this.bulkRoomBooking = new BulkRoomBooking(22, this.arrival, this.departure);
		this.bulkRoomBooking.getReferences().add(REFERENCE);
	}

	@Test
	public void sucess(@Mocked final HotelInterface hotelInterface){
		RoomBookingData roomBookingData = new RoomBookingData();
		roomBookingData.setRoomType(TYPE);
		new Expectations() {{
			HotelInterface.getRoomBookingData(REFERENCE);
			this.result = roomBookingData;
		}};
		
		this.bulkRoomBooking.getReference(TYPE);
		for (String ref: this.bulkRoomBooking.getReferences()) 
			Assert.assertNotEquals(ref, roomBookingData.getReference());
		
		Assert.assertFalse(this.bulkRoomBooking.isCancelled());
		Assert.assertEquals(0, this.bulkRoomBooking.getNumOfRemoteErrors());
	}
	
	@Test
	public void throwHotelException(@Mocked final HotelInterface hotelInterface){
		new Expectations() {{
			HotelInterface.getRoomBookingData(REFERENCE);
			this.result = new HotelException();
		}};
		this.bulkRoomBooking.getReference(TYPE);
		
		Assert.assertFalse(this.bulkRoomBooking.isCancelled());
		Assert.assertEquals(1, this.bulkRoomBooking.getReferences().size());
		Assert.assertEquals(0, this.bulkRoomBooking.getNumOfRemoteErrors());
	}

	@Test
	public void tenRemoteAccessException(@Mocked final HotelInterface hotelInterface){
		new Expectations() {{
			HotelInterface.getRoomBookingData(REFERENCE);				
			for(int i=0;i<10;i++)
				this.result = new RemoteAccessException(); 
			this.times = 10;
		}};
		
		for(int i=0;i<9;i++) 
			this.bulkRoomBooking.getReference(TYPE);
		Assert.assertFalse(this.bulkRoomBooking.isCancelled());
		
		this.bulkRoomBooking.getReference(TYPE);
		Assert.assertTrue(this.bulkRoomBooking.isCancelled());
		
		Assert.assertEquals(10, this.bulkRoomBooking.getNumOfRemoteErrors());
	}

	@Test
	public void lessThenTenRemoteAccessException(@Mocked final HotelInterface hotelInterface){
		RoomBookingData roomBookingData = new RoomBookingData();
		roomBookingData.setRoomType(TYPE);
		new StrictExpectations() {{
			HotelInterface.getRoomBookingData(REFERENCE);	
			for(int i=0;i<9;i++) 
				this.result = new RemoteAccessException();
			this.result = roomBookingData;		
			this.times = 10;
		}};
		
		for(int i=0;i<9;i++) 
			this.bulkRoomBooking.getReference(TYPE);
		Assert.assertEquals(9, this.bulkRoomBooking.getNumOfRemoteErrors());
		
		this.bulkRoomBooking.getReference(TYPE);
		Assert.assertEquals(0, this.bulkRoomBooking.getReferences().size());
		Assert.assertFalse(this.bulkRoomBooking.isCancelled());
		
		for (String ref: this.bulkRoomBooking.getReferences()) 
			Assert.assertNotEquals(ref, roomBookingData.getReference());
	}		

	@Test
	public void HotelExceptionAfterNineRemoteAccessException(@Mocked final HotelInterface hotelInterface){
		RoomBookingData roomBookingData = new RoomBookingData();
		roomBookingData.setRoomType(TYPE);
		new StrictExpectations() {{
			HotelInterface.getRoomBookingData(REFERENCE);				
			for(int i=0 ; i<9 ; i++) 
				this.result = new RemoteAccessException(); 
			this.result = new HotelException();
			this.times = 10;
		}};
		
		for(int i=0 ; i<9 ; i++)
			this.bulkRoomBooking.getReference(TYPE);
		Assert.assertEquals(9, this.bulkRoomBooking.getNumOfRemoteErrors());
		Assert.assertFalse(this.bulkRoomBooking.isCancelled()); 
		
		this.bulkRoomBooking.getReference(TYPE);
		Assert.assertEquals(0, this.bulkRoomBooking.getNumOfRemoteErrors());
		
		Assert.assertEquals(1, this.bulkRoomBooking.getReferences().size());
	}
/*
	@Test
	public void RemoteAccessExceptionsAndHotelExceptionInBetween(@Mocked final HotelInterface hotelInterface){
		RoomBookingData roomBookingData = new RoomBookingData();
		roomBookingData.setRoomType(TYPE);
		new StrictExpectations() {{
			HotelInterface.getRoomBookingData(REFERENCE);				
			for(int i=0 ; i<9 ; i++)
				this.result = new RemoteAccessException(); 			
			this.result = new HotelException();				
			for(int i=0 ; i<10 ; i++) 
				this.result = new RemoteAccessException();
			this.times = 20;
		}};
		
		for(int i=0 ; i<9 ; i++) 
			this.bulkRoomBooking.getReference(TYPE);
		Assert.assertEquals(9, this.bulkRoomBooking.getNumOfRemoteErrors());
		
		this.bulkRoomBooking.getReference(TYPE);
		Assert.assertEquals(0, this.bulkRoomBooking.getNumOfRemoteErrors());
		Assert.assertFalse(this.bulkRoomBooking.isCancelled()); 

		for(int i=0 ; i<10 ; i++) 
			this.bulkRoomBooking.getReference(TYPE);
		Assert.assertEquals(10, this.bulkRoomBooking.getNumOfRemoteErrors());
		Assert.assertTrue(this.bulkRoomBooking.isCancelled()); 
		}
	*/
}
