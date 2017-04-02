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

		private final LocalDate begin = new LocalDate(2018, 12, 19);
		private final LocalDate end = new LocalDate(2018, 12, 21);

		private ActivityProvider provider;
		private Activity activity;
		private ActivityOffer offer;
		private Booking booking;

		@Before
		public void setUp() {
			this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
			this.activity = new Activity(this.provider, "Bush Walking", 20, 80, 25);
			this.offer = new ActivityOffer(this.activity, this.begin, this.end);
			this.booking = new Booking(this.provider, this.offer);
		}
		
		@Test(expected=ActivityException.class)
		public void nullString(){
			provider.cancelReservation(null);
		}
		
		@Test(expected=ActivityException.class)
		public void emptyString(){
			provider.cancelReservation("   ");
		}
		
		@Test(expected=ActivityException.class)
		public void invalidString(){
			provider.cancelReservation("\n");
		}
		
		@Test(expected=ActivityException.class)
		public void invalidString2(){
			provider.cancelReservation("\t");
		}
		
		@Test(expected=ActivityException.class)
		public void referenceDoesntExist(){
			this.provider.cancelReservation("REFERENCE_XPTO_DOESNT_EXIST");
		}
		
		@Test
		public void sucessCancelReservation(){
			String cancelResult = this.provider.cancelReservation(this.booking.getReference());
			
			Assert.assertEquals(cancelResult, this.booking.getCancellationReference());
		}
		
		
		@Test(expected=ActivityException.class)
		public void cancelCancelledReservation(){
			this.provider.cancelReservation(this.booking.getReference());
			this.provider.cancelReservation(this.booking.getReference());
		}
		
		@Test(expected=ActivityException.class)
		public void cancelOldActivity(){
			LocalDate oldBegin = new LocalDate(1990, 12, 19);
			LocalDate oldEnd = new LocalDate(1990, 12, 29);
			ActivityOffer oldOffer = new ActivityOffer(this.activity, oldBegin, oldEnd);
			Booking oldBooking = new Booking(this.provider, oldOffer);
			this.provider.cancelReservation(oldBooking.getReference());
		}
		
		@After
		public void tearDown() {
			ActivityProvider.providers.clear();
		}
		

}
