package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityOfferHasVacancyMethodTest {
	private ActivityOffer offer;
	private ActivityProvider provider;

	@Before
	public void setUp() {
		provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(provider, "Bush Walking", 18, 80, 3);

		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		this.offer = new ActivityOffer(activity, begin, end);
	}

	@Test
	public void successZeroBookinks() {
		Assert.assertTrue(this.offer.hasVacancy());
	}
	
	@Test(expected = ActivityException.class)
	public void capacityFull() {
		Booking booking;
		while(offer.hasVacancy())
			booking = new Booking(this.provider, this.offer);
		booking = new Booking(this.provider, this.offer);	
	}


	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
