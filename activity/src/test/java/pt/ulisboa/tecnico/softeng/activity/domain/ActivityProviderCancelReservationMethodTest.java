package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityProviderCancelReservationMethodTest {

		private final LocalDate begin = new LocalDate(2016, 12, 19);
		private final LocalDate end = new LocalDate(2016, 12, 21);

		private ActivityProvider provider;
		private Activity activity;
		private ActivityOffer offer;
		private Booking booking;
		private String reference;

		@Before
		public void setUp() {
			this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
			this.activity = new Activity(this.provider, "Bush Walking", 20, 80, 25);
			this.offer = new ActivityOffer(this.activity, this.begin, this.end);
			reference = ActivityProvider.reserveActivity(this.begin, this.end, 30);
		}
		
		@Test(expected=ActivityException.class)
		public void nullString(){
			ActivityProvider.cancelReservation(null);
		}
		
		@Test(expected=ActivityException.class)
		public void emptyString(){
			ActivityProvider.cancelReservation("   ");
		}
		
		@Test(expected=ActivityException.class)
		public void invalidString(){
			ActivityProvider.cancelReservation("\n");
		}
		
		@Test(expected=ActivityException.class)
		public void invalidString2(){
			ActivityProvider.cancelReservation("\t");
		}
		
		@Test(expected=ActivityException.class)
		public void referenceDoesntExist(){
			ActivityProvider.cancelReservation("REFERENCE_XPTO_DOESNT_EXIST");
		}
		
		@Test
		public void sucessCancelReservation(){
			String cancelResult = ActivityProvider.cancelReservation(reference);
			Booking booking = this.offer.getBooking(reference);
			Assert.assertEquals(cancelResult, booking.getCancellationReference());
		}
		
		
		@Test(expected=ActivityException.class)
		public void cancelCancelledReservation(){
			ActivityProvider.cancelReservation(reference);
			ActivityProvider.cancelReservation(reference);
		}
		
		
		@After
		public void tearDown() {
			ActivityProvider.providers.clear();
		}
		

}
