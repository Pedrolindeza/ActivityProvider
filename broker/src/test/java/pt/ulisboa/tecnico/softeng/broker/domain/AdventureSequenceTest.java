package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class AdventureSequenceTest {
	private static final String IBAN = "BK01987654321";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String ROOM_CANCELLATION = "RoomCancellation";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 20);
	
	private Adventure adventure;
	
	@Injectable
	private Broker broker;
	
	@Before
	public void setUp() {}

	@Test 
	public void ActivityReservation(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface) {
		this.adventure = new Adventure(this.broker, this.begin, this.begin, 20, IBAN, 300);

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, 300);
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(), adventure.getAge());
			}
		};

		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());

		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}

	@Test 
	public void AdventureReservation(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, 
		@Mocked final HotelInterface hotelInterface) {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);

		new StrictExpectations(){
			{
				BankInterface.processPayment(IBAN, 300);
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(), adventure.getAge());
				HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());

		this.adventure.process();
		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());

		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void failedPaymentGoToCancelled(@Mocked final BankInterface bankInterface) {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);

		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, 300);
				this.result = new BankException();
			}
		};

		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void paymentFailedDueToRemoteErrors(@Mocked final BankInterface bankInterface) {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, 300);
				this.result = new RemoteAccessException();
				BankInterface.processPayment(IBAN, 300);
				this.result = new RemoteAccessException();
				BankInterface.processPayment(IBAN, 300);
				this.result = new RemoteAccessException();
			}
		};
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void testUndoToCancelled(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, 
			 @Mocked final HotelInterface hotelInterface) {	
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);

		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setActivityCancellation(ACTIVITY_CANCELLATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setRoomCancellation(ROOM_CANCELLATION);
		
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, 300);
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(), adventure.getAge());
				HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
				this.result = new HotelException();
			}
		};
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	
}

