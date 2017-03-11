package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class AdventureConstructorMethodTest {
	private Broker broker;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE");
	}

	@Test
	public void success() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		Adventure adventure = new Adventure(this.broker, begin, end, 20, "BK011234567", 300);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(begin, adventure.getBegin());
		Assert.assertEquals(end, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals("BK011234567", adventure.getIBAN());
		Assert.assertEquals(300, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getBankPayment());
		Assert.assertNull(adventure.getActivityBooking());
		Assert.assertNull(adventure.getRoomBooking());
	}
	
	/* Argumentos Inconsistentes */
	
	@SuppressWarnings("unused")
	@Test
	public void invalideDates(){
		
		exception.expect(BrokerException.class);
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		Adventure adventure = new Adventure(this.broker, end, begin, 20, "BK011234567", 300);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void nullArgs(){
		exception.expect(BrokerException.class);
		Adventure adventure = new Adventure(null, null, null, 0, "", 0);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void underAge(){
		exception.expect(BrokerException.class);
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		Adventure adventure = new Adventure(this.broker, begin, end, 5, "BK011234567", 300);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void negativeAmount(){
		exception.expect(BrokerException.class);
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		Adventure adventure = new Adventure(this.broker, begin, end, 5, "BK011234567", -25);
	}
	
	
	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
