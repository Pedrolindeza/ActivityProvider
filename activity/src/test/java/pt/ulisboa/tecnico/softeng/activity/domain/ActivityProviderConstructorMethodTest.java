package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.Rule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityProviderConstructorMethodTest {

	@Rule
    public ExpectedException exception = ExpectedException.none();

	@Test
	public void success() {
		ActivityProvider provider = new ActivityProvider("XtremX", "Adventure++");

		Assert.assertEquals("Adventure++", provider.getName());
		Assert.assertTrue(provider.getCode().length() == ActivityProvider.CODE_SIZE);
		Assert.assertEquals(1, ActivityProvider.providers.size());
		Assert.assertEquals(0, provider.getNumberOfActivities());
	}

	@Test // -> excepted = ActivityException.class
	public void nullActivityProvider(){
		exception.expect(NullPointerException.class);
		new ActivityProvider(null,null);
	}

	@Test//HEYY  excepted = ActivityException.class
	public void emptyCode(){
		exception.expect(ActivityException.class);
		new ActivityProvider("","Adventure++");
	}

	@Test//HEYY excepted = ActivityException.class
	public void blankCode(){
		exception.expect(ActivityException.class);
		new ActivityProvider(" ","Adventure++");
	}

	@Test//HEYY excepted = ActivityException.class
	public void bigCode(){
		exception.expect(ActivityException.class);
		new ActivityProvider("XtremXX","Adventure++");
	}

	@Test//HEYY excepted = ActivityException.class
	public void smallCode(){
		exception.expect(ActivityException.class);
		new ActivityProvider("Xtrem","Adventure++");
	}
	@Test//-> excepted = ActivityException.class
	public void notUniqueCode(){
		exception.expect(ActivityException.class);
		new ActivityProvider("XtremX","Adventure+");
		new ActivityProvider("XtremX","Adventure++");
	}

	@Test//-> excepted = ActivityException.class
	public void notUniqueName(){
		exception.expect(ActivityException.class);
		new ActivityProvider("XtremZ","Adventure++");
		new ActivityProvider("XtremY","Adventure++");
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
