package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class GetActivityReservationDataMethodTest{
	ActivityProvider provider;
	Activity activity;
	ActivityOffer offer;
	Booking booking;
	private final LocalDate begin = new LocalDate(2016, 5, 5);
	private final LocalDate end = new LocalDate(2016,10, 10);

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "Adventure++");
		this.activity = new Activity(this.provider, "Bush Walking", 18, 80, 25);
		this.offer = new ActivityOffer(this.activity, this.begin,this.end);
		this.booking = new Booking(this.provider,this.offer);
	}
	

	@Test
	public void success() {
		String reference = booking.getReference();
		ActivityReservationData ard = ActivityProvider.getActivityReservationData(reference);

		Assert.assertEquals(ard.getReference(),reference);
		Assert.assertEquals(ard.getName(),this.activity.getName());
		Assert.assertEquals(ard.getCode(),this.activity.getCode());
		Assert.assertEquals(ard.getBegin(),this.offer.getBegin());
		Assert.assertEquals(ard.getEnd(),this.offer.getEnd());
	}
	
	@Test(expected = ActivityException.class)
	public void nullReference() {
		ActivityProvider.getActivityReservationData(null);
	}

	@Test(expected = ActivityException.class)
	public void emptyReference() {
		ActivityProvider.getActivityReservationData("");
	}

	@Test(expected = ActivityException.class)
	public void blankReference() {
		ActivityProvider.getActivityReservationData(" ");
	}

	@Test(expected = ActivityException.class)
	public void endLineReference() {
		ActivityProvider.getActivityReservationData("\0");
	}
	
	@Test(expected = ActivityException.class)
	public void newLineReference() {
		ActivityProvider.getActivityReservationData("\n");
	}
	
	@Test(expected = ActivityException.class)
	public void nonExistanceReference() {
		ActivityProvider.getActivityReservationData("afsgfif");
	}
	

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}