package pt.ulisboa.tecnico.softeng.activity.services.local;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData.CopyDepth;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityReservationData;


public class ActivityInterface {

	@Atomic(mode = TxMode.WRITE)
	public static String reserveActivity(LocalDate begin, LocalDate end, int age) {
		List<ActivityOffer> offers;
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			offers = provider.findOffer(begin, end, age);
			if (!offers.isEmpty()) {
				return new Booking(offers.get(0)).getReference();
			}
		}
		throw new ActivityException();
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static ActivityProvider createActivityProvider(ActivityProviderData activityProviderData) {
		return new ActivityProvider(activityProviderData.getCode(), activityProviderData.getName());
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static List<ActivityProviderData> getActivityProviders() {
		List<ActivityProviderData> activityProviders = new ArrayList<>();
		for (ActivityProvider activityProvider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			activityProviders.add(new ActivityProviderData(activityProvider, CopyDepth.SHALLOW));
		}
		return activityProviders;
	}

	
	@Atomic(mode = TxMode.WRITE)
	public static Activity createActivity(String providerCode, ActivityData activityData) {
		return new Activity(ActivityInterface.getActivityProviderByCode(providerCode), activityData.getName(), activityData.getMinAge(), activityData.getMaxAge(), activityData.getCapacity());
	}
		
	
	@Atomic(mode = TxMode.WRITE)
	public static ActivityOffer createActivityOffer(ActivityOfferData activityOfferData) {
		return new ActivityOffer(activityOfferData.getActivity(), activityOfferData.getBegin(), activityOfferData.getEnd() );
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static List<ActivityData> getActivityDatasByProvider(String providerCode) {
		List<ActivityData> activities = new ArrayList<>();
		for (ActivityData activityData : getActivityProviderDataByCode(providerCode, CopyDepth.OFFER).getActivities()) {
			activities.add(activityData);
		}
		return activities;
	}
	
	/*@Atomic(mode = TxMode.WRITE)
	public static List<ActivityOfferData> getActivityOffers() {
		List<ActivityOfferData> activityOffers = new ArrayList<>();
		for (ActivityOffer activityOffer : FenixFramework.getDomainRoot().getActivityOfferSet()) {
			activityOffers.add(new ActivityOfferData(activityOffer));
		}
		return activityOffers;
	}*/
	
	@Atomic(mode = TxMode.WRITE)
	public static String cancelReservation(String reference) {
		Booking booking = getBookingByReference(reference);
		if (booking != null) {
			return booking.cancel();
		}
		throw new ActivityException();
	}

	@Atomic(mode = TxMode.READ)
	public static ActivityReservationData getActivityReservationData(String reference) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			for (Activity activity : provider.getActivitySet()) {
				for (ActivityOffer offer : activity.getActivityOfferSet()) {
					Booking booking = offer.getBooking(reference);
					if (booking != null) {
						return new ActivityReservationData(provider, offer, booking);
					}
				}
			}
		}
		throw new ActivityException();
	}
	@Atomic(mode = TxMode.READ)
	private static Booking getBookingByReference(String reference) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			Booking booking = provider.getBooking(reference);
			if (booking != null) {
				return booking;
			}
		}
		return null;
	}
	
	@Atomic(mode = TxMode.READ)
	private static ActivityProvider getActivityProviderByCode(String code){
		for (ActivityProvider provider: FenixFramework.getDomainRoot().getActivityProviderSet()){
			if (provider.getCode().equals(code)){
				return provider;
			}
		}
		return null;
	}
	
	@Atomic(mode = TxMode.READ)
	public	static List<Activity> getActivitiesByProvider(String providerCode){
		List<Activity> list= new ArrayList<>();
		for (ActivityProvider provider: FenixFramework.getDomainRoot().getActivityProviderSet()){
			if (provider.getCode().equals(providerCode)){
				for (Activity activity : provider.getActivitySet()) {
						list.add(activity);
				}
			}
		}
		return list;
	}
	
	@Atomic(mode = TxMode.READ)
	public static ActivityProviderData getActivityProviderDataByCode(String providerCode, CopyDepth depth){
		ActivityProvider provider = getActivityProviderByCode(providerCode);
		
		if (provider != null){
			return new ActivityProviderData(provider, depth);	
		}
		
		else{
			return null;
		}
	}
	
	@Atomic(mode = TxMode.READ)
	public static ActivityData getActivityDataByCode(String reference) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			for (Activity activity : provider.getActivitySet()) {
				if(activity.getCode() == reference)
					return new ActivityData(activity);
			}
		}	
		return null;
	}

}
