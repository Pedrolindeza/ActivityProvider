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
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class UndoStateProcessMethodTest {
	private static final String IBAN = "BK01987654321";
	private static final int AMOUNT = 300;
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;


	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, AMOUNT);
		this.adventure.setState(State.UNDO);
		
	}
	
	@Test
	public void undoPaymentHotelException(@Mocked final BankInterface bankInterface){
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = new HotelException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void undoPaymentRemoteAccessException(@Mocked final BankInterface bankInterface){
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	
	@Test
	public void undoActivityException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface) {
		
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
	
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				
				this.result = new HotelException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void undoActivityRemoteAccessException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface) {
		
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
	
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void undoRoomException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				
				this.result = new HotelException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	
	@Test
	public void undoRoomRemoteAccessException(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	
	@Test
	public void undoPayment(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void undoActivity(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface) {
		
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
	
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void undoRoom(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	
	
	@Test
	public void undoStateTest(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		
		this.adventure.process();
	 		
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	 	}
	
}