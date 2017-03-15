package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityProviderFindOfferMethodTest {
	private ActivityProvider provider;
	private ActivityOffer offer;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 25);

		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		this.offer = new ActivityOffer(activity, begin, end);
	}

	@Test
	public void success() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		Set<ActivityOffer> offers = this.provider.findOffer(begin, end, 40);

		Assert.assertEquals(1, offers.size());
		Assert.assertTrue(offers.contains(this.offer));
	}

	@Test(expected = ActivityException.class)
	public void beginNull(){
		LocalDate end = new LocalDate(2016, 12, 21);
		this.provider.findOffer(null, end, 40);
	}

	@Test(expected = ActivityException.class)
	public void endNull(){
		LocalDate begin = new LocalDate(2016, 12, 19);
		this.provider.findOffer(begin, null, 40);
	}

	@Test(expected = ActivityException.class)
	public void zeroAge(){
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		this.provider.findOffer(begin, end, 0);
	}

	@Test(expected = ActivityException.class)
	public void incompatibleDate(){
		LocalDate begin = new LocalDate(2016, 12, 21);
		LocalDate end = new LocalDate(2016, 12, 19);
		this.provider.findOffer(begin, end, 0);
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}
}
