package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityProvider {
	public static Set<ActivityProvider> providers = new HashSet<>();

	static final int CODE_SIZE = 6;

	private final String name;
	private final String code;
	private final Set<Activity> activities = new HashSet<>();

	public ActivityProvider(String code, String name) {
		checkArguments(code, name);

		this.code = code;
		this.name = name;

		ActivityProvider.providers.add(this);
	}

	private void checkArguments(String code, String name) {
		if (code == null || name == null || code.trim().equals("") || name.trim().equals("")) {
			throw new ActivityException();
		}

		if (code.length() != ActivityProvider.CODE_SIZE) {
			throw new ActivityException();
		}

		for (ActivityProvider activityProvider : providers) {
			if (activityProvider.getCode().equals(code) || activityProvider.getName().equals(name)) {
				throw new ActivityException();
			}
		}
	}

	String getName() {
		return this.name;
	}

	String getCode() {
		return this.code;
	}

	int getNumberOfActivities() {
		return this.activities.size();
	}

	void addActivity(Activity activity) {
		this.activities.add(activity);
	}

	public List<ActivityOffer> findOffer(LocalDate begin, LocalDate end, int age) {
		List<ActivityOffer> result = new ArrayList<>();
		for (Activity activity : this.activities) {
			result.addAll(activity.getOffers(begin, end, age));
		}
		return result;
	}

	public static String reserveActivity(LocalDate begin, LocalDate end, int age) {
		List<ActivityOffer> offers;
		for (ActivityProvider provider : ActivityProvider.providers) {
			offers = provider.findOffer(begin, end, age);
			if (!offers.isEmpty()) {
				return new Booking(provider, offers.get(0)).getReference();
			}
		}
		throw new ActivityException();
	}

	public Set<Activity> getActivity(){
	 		return activities;
	 	}

	public static String cancelReservation(String activityConfirmation) throws ActivityException {
		if(activityConfirmation == null || activityConfirmation.trim().equals(""))
			throw new ActivityException();
		
		for(ActivityProvider provider : providers){
			Set<Activity> activities = provider.getActivity();
			for(Activity activity : activities){
				Set<ActivityOffer> offers = activity.getOffer();
				for(ActivityOffer offer : offers){
					
						String cancelResult = offer.cancelReserve(activityConfirmation);
						if(cancelResult != null) return cancelResult;
					
					
				}
			}
		}
		
		throw new ActivityException("Reference Doesn't Exist or Activity already started");
	}


	public static ActivityReservationData getActivityReservationData(String reference) throws ActivityException{
		// TODO implement
		if(reference==null || reference=="" || reference==" " || reference=="\n" || reference=="\0")
			throw new ActivityException();
		ActivityReservationData ard = new ActivityReservationData();
		for (ActivityProvider activityProvider : providers) {
				Set<Activity> activities = activityProvider.getActivity();
				for(Activity activity : activities){
					Set<ActivityOffer> offers =activity.getOffer();
					for(ActivityOffer offer : offers){
						Booking booking = offer.getBooking(reference);
						if(booking!=null){
 						ard.setReference(reference);
 						ard.setName(activity.getName());
 						ard.setCode(activity.getCode());
 						ard.setBegin(offer.getBegin());
 						ard.setEnd(offer.getEnd());
 						return ard;
 					}
				
			}
		}
	}
		throw new ActivityException();
	}
	
}

