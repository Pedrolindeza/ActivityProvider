package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.Rule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityProviderConstructorMethodTest {

	@Test
	public void success() {
		ActivityProvider provider = new ActivityProvider("XtremX", "Adventure++");

		Assert.assertEquals("Adventure++", provider.getName());
		Assert.assertTrue(provider.getCode().length() == ActivityProvider.CODE_SIZE);
		Assert.assertEquals(1, ActivityProvider.providers.size());
		Assert.assertEquals(0, provider.getNumberOfActivities());
	}

	@Test(expected = ActivityException.class)
	public void nullCodeActivityProvider(){
		new ActivityProvider(null,"Adventure++");
	}

	@Test(expected = ActivityException.class)
	public void nullNameActivityProvider(){
		new ActivityProvider("XtremX",null);
	}

	@Test(expected = ActivityException.class)
	public void emptyCode(){
		new ActivityProvider("","Adventure++");
	}

	@Test(expected = ActivityException.class)
	public void emptyName(){
		new ActivityProvider("XtremX","");
	}

	@Test(expected = ActivityException.class)
	public void blankCode(){
		new ActivityProvider(" ","Adventure++");
	}

	@Test(expected = ActivityException.class)
	public void blankName(){
		new ActivityProvider("XtremX"," ");
	}
	
	@Test(expected = ActivityException.class)
	public void bigCode(){
		new ActivityProvider("XtremXX","Adventure++");
	}

	@Test(expected = ActivityException.class)
	public void smallCode(){
		new ActivityProvider("Xtrem","Adventure++");
	}
	@Test(expected = ActivityException.class)
	public void notUniqueCode(){
		new ActivityProvider("XtremX","Adventure+");
		new ActivityProvider("XtremX","Adventure++");
	}

	@Test(expected = ActivityException.class)
	public void notUniqueName(){
		new ActivityProvider("XtremX","Adventure++");
		new ActivityProvider("XtremY","Adventure++");
	}

	
	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}