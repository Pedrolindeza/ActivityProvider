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
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class ConfirmedStateProcessMethodTest {
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String ROOM_CANCELLATION = "RoomCancellation";
	private static final String IBAN = "BK01987654321";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		this.adventure.setState(State.CONFIRMED);
	}
	
	@Test
	public void fiveBankException(@Mocked final BankInterface bankInterface) {

		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);
		
		new Expectations() {
			{
				BankInterface.getOperationData(adventure.getPaymentConfirmation());
				this.result = new BankException();
			}
		};
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
		
		
		this.adventure.process();
		

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void twentyRaException(@Mocked final BankInterface bankInterface) {

		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);
		
		new Expectations() {
			{
				BankInterface.getOperationData(adventure.getPaymentConfirmation());
				this.result = new RemoteAccessException();
			}
		};
		for(int i=0; i<19; i++)
		this.adventure.process();
		
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());

		this.adventure.process();
		
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void ActException(@Mocked final ActivityInterface activityInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setActivityCancellation(ACTIVITY_CANCELLATION);
		
		new Expectations() {
			{	

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();
			}
		};
		this.adventure.process();
		

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void ActivityRaException( @Mocked final ActivityInterface activityInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		for(int j=0; j<20; j++)
		this.adventure.process();
		

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void HotelRException(@Mocked final HotelInterface hotelInterface) {
		
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new Expectations() {
			{
				HotelInterface.getRoomBookingData(adventure.getRoomConfirmation());
				this.result = new HotelException();
			}
		};
		this.adventure.process();
		

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void twentyHotelRaException(@Mocked final HotelInterface hotelInterface) {

		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new Expectations() {
			{
				HotelInterface.getRoomBookingData(adventure.getRoomConfirmation());
				this.result = new RemoteAccessException();
			}
		};
		for(int i=0; i<20; i++)
		this.adventure.process();
		

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

}
