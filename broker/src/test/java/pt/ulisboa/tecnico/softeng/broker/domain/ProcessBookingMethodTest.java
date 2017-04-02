package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.broker.domain.BulkRoomBooking;

@RunWith(JMockit.class)
public class ProcessBookingMethodTest {
	private static final int NUMBER = 3;
	private static final int NUMERO_DE_REMOTE_ERRORS = 10;
	private static final int MAX_HOTEL_EXCEPTIONS = 3;

	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private static boolean cancelled = false;
	private BulkRoomBooking bulkroom;


	
	@Injectable
	private Broker broker;
	
	@Before
	public void setUp() {

		this.bulkroom= new BulkRoomBooking(NUMBER, this.arrival, this.departure);
		this.bulkroom.resetCancelled();
		this.bulkroom.resetNumOfHotelErrors();
		this.bulkroom.resetNumOfRemoteErrors();
			
	}
	@Test
	public void remoteException(@Mocked final HotelInterface hotelInterface){
		
		new Expectations() {
			{
				HotelInterface.bulkBooking(bulkroom.getNumber(),bulkroom.getArrival(),bulkroom.getDeparture());
				this.result = new RemoteAccessException();
				this.times = NUMERO_DE_REMOTE_ERRORS;
			}
		};
		for (int i = 0 ; i < NUMERO_DE_REMOTE_ERRORS; i++){
			this.bulkroom.processBooking();
		}
		

		Assert.assertTrue(this.bulkroom.isCancelled());
	}


	
	@Test
	public void tenRemoteAccessException(@Mocked final HotelInterface hotelInterface){

		new Expectations() {
			{
				HotelInterface.bulkBooking(bulkroom.getNumber(),bulkroom.getArrival(),bulkroom.getDeparture());
				this.result = new RemoteAccessException();
				this.times = NUMERO_DE_REMOTE_ERRORS;
			}
		};
		for(int i=0;i <= NUMERO_DE_REMOTE_ERRORS ;i++){
			this.bulkroom.processBooking();
		}

		Assert.assertTrue(this.bulkroom.isCancelled());
	}
	
	@Test
	public void lessthanHotelException(@Mocked final HotelInterface hotelInterface){
		
		new Expectations() {
			{
				HotelInterface.bulkBooking(bulkroom.getNumber(),bulkroom.getArrival(),bulkroom.getDeparture());
				this.result = new HotelException();
				this.times = MAX_HOTEL_EXCEPTIONS-1;
			}
		};
		for(int i=0;i< MAX_HOTEL_EXCEPTIONS -1 ;i++){
			this.bulkroom.processBooking();
		}

		Assert.assertFalse(this.bulkroom.isCancelled());
	}

	@Test
	public void threeHotelException(@Mocked final HotelInterface hotelInterface){
		
		new Expectations() {
			{
				HotelInterface.bulkBooking(bulkroom.getNumber(),bulkroom.getArrival(),bulkroom.getDeparture());
				this.result = new HotelException();
				this.times = MAX_HOTEL_EXCEPTIONS;
			}
		};
		for(int i=0;i<MAX_HOTEL_EXCEPTIONS;i++){
			this.bulkroom.processBooking();
		}
		Assert.assertTrue(this.bulkroom.isCancelled());
	}
	

	@Test
	public void success(@Mocked final HotelInterface hotelInterface){
		
		new Expectations() {
			{
			HotelInterface.bulkBooking(bulkroom.getNumber(),bulkroom.getArrival(),bulkroom.getDeparture());
				
			}
		};
		this.bulkroom.processBooking();

		Assert.assertFalse(this.bulkroom.isCancelled());
	}
}