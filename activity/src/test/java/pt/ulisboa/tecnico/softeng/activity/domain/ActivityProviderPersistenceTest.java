package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.Atomic.TxMode;

public class ActivityProviderPersistenceTest {
	private static final String PROVIDER_NAME = "FAST_TRAVELLL";
	private static final String PROVIDER_CODE = "code00";
	private static final String ACTIVITY_NAME = "Running11";
	private static final int MIN_AGE = 18;
	private static final int MAX_AGE = 90;
	private static final int CAPACITY = 10;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}
	
	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		ActivityProvider activityProvider = new ActivityProvider(PROVIDER_CODE, PROVIDER_NAME);
		Activity activity = new Activity(activityProvider, ACTIVITY_NAME, MIN_AGE, MAX_AGE, CAPACITY);
		ActivityOffer activityOffer = new ActivityOffer(activity, this.begin, this.end);
		new Booking(activityProvider, activityOffer);
	}
	
	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		assertEquals(1, FenixFramework.getDomainRoot().getActivityProviderSet().size());

		List<ActivityProvider> providers  = new ArrayList<>(FenixFramework.getDomainRoot().getActivityProviderSet());
		ActivityProvider provider = providers.get(0);
		
		assertEquals(PROVIDER_NAME, provider.getName());
		assertEquals(PROVIDER_CODE, provider.getCode());
		
		List<Activity> activities = new ArrayList<>(provider.getActivitySet());
		Activity activity = activities.get(0);

		assertEquals(ACTIVITY_NAME, activity.getName());
		assertEquals(1, activity.getActivityOfferSet().size());

		List<ActivityOffer> activityOffers = new ArrayList<>(activity.getActivityOfferSet());
		ActivityOffer activityOffer = activityOffers.get(0);

		assertEquals(this.begin, activityOffer.getBegin());
		assertEquals(this.end, activityOffer.getEnd());

		List<Booking> bookings = new ArrayList<>(activityOffer.getBookingSet());
		Booking booking = bookings.get(0);
		
		assertNotNull(booking.getReference());
	}
	

   @After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
	   for (ActivityProvider pro : FenixFramework.getDomainRoot().getActivityProviderSet()) {
		   for(Activity act : pro.getActivitySet()){
			   for(ActivityOffer offer : act.getActivityOfferSet()){
				   for(Booking booking : offer.getBookingSet()){
					   booking.delete();
				   }
				   offer.delete();
			   }
			   act.delete();
		   }
			pro.delete();
		}

   }

}
