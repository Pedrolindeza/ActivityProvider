package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class BookRoomStateProcessMethodTest {
	
	private static final String IBAN = "BK01987654321";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;
	
	
	@Injectable
	private Broker broker;
	
	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		this.adventure.setState(State.BOOK_ROOM);
	}
	
	@Test
	public void simpleBook(@Mocked final HotelInterface hotelinterface){
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void reservationFailed(@Mocked final HotelInterface hotelinterface){
		new Expectations() {
			{
				HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
				this.result = new HotelException();
			}
		};
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void failByErrors(@Mocked final HotelInterface hotelinterface){
		new Expectations() {
			{
				HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
				this.result = new RemoteAccessException();
				this.times = 10;	
			}
		};
		
		for(int i=0; i < 9; i++){
			this.adventure.process();
			Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		}
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	
	@Test
	public void underControlFails(@Mocked final HotelInterface hotelInterface) {
		new Expectations() {
			{
				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end);
				this.result = new Delegate<Object>() {
					private int times = 1;

					@SuppressWarnings("unused")
					String delegate(Room.Type type, LocalDate arrival, LocalDate departure) {
						
						if (times < 10){
							times++;
							throw new RemoteAccessException();
						}
						else
							return "the end";
					}
				};
				this.times = 10;
			}
		};
		for (int i = 0; i < 9; i++) {
			this.adventure.process();
			Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		}
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());

	}	
}
