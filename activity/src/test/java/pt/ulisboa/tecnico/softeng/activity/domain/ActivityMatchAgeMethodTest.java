package pt.ulisboa.tecnico.softeng.activity.domain;

import java.time.format.DateTimeFormatter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActivityMatchAgeMethodTest {
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	private Activity activity;

	@Before
	public void setUp() {
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(provider, "Bush Walking", 18, 80, 3);
	}

	@Test
	public void successIn() {
		Assert.assertTrue(this.activity.matchAge(50));
	}
	
	@Test
	public void ageBelowTest() {
		Assert.assertFalse(this.activity.matchAge(10));
	}
	
	@Test
	public void ageAboveTest() {
		Assert.assertFalse(this.activity.matchAge(90));
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
