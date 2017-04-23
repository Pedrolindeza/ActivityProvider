package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ist.fenixframework.FenixFramework;

public class Activity extends Activity_Base{
	private static final int MIN_AGE = 18;
	private static final int MAX_AGE = 100;

	private static int counter = 0;
/*
	private final String name;
	private final String code;
	private final int minAge;
	private final int maxAge;
	private final int capacity;
*/
	private final Set<ActivityOffer> offers = new HashSet<>();

	public Activity(ActivityProvider provider, String name, int minAge, int maxAge, int capacity) {
		checkArguments(provider, name, minAge, maxAge, capacity);

		setCode(provider.getCode() + Integer.toString(++Activity.counter));
		setName(name);
		setMinAge(minAge);
		setMaxAge(maxAge);
		setCapacity(capacity);

		provider.addActivity(this);
	}

	private void checkArguments(ActivityProvider provider, String name, int minAge, int maxAge, int capacity) {
		if (provider == null || name == null || name.trim().equals("")) {
			throw new ActivityException();
		}

		if (minAge < MIN_AGE || maxAge >= MAX_AGE || minAge > maxAge) {
			throw new ActivityException();
		}

		if (capacity < 1) {
			throw new ActivityException();
		}

	}
/*
	String getName() {
		return this.name;
	}

	String getCode() {
		return this.code;
	}

	int getMinAge() {
		return this.minAge;
	}

	int getMaxAge() {
		return this.maxAge;
	}

	int getCapacity() {
		return this.capacity;
	}
*/
	int getNumberOfOffers() {
		return getActivityOfferSet().size();
	}

	void addOffer(ActivityOffer offer) {
		addActivityOffer(offer);
	}

	Set<ActivityOffer> getOffers(LocalDate begin, LocalDate end, int age) {
		Set<ActivityOffer> result = new HashSet<>();
		for (ActivityOffer offer : getActivityOfferSet()) {
			if (matchAge(age) && offer.available(begin, end)) {
				result.add(offer);
			}
		}
		return result;
	}

	boolean matchAge(int age) {
		return age >= getMinAge() && age <= getMaxAge();
	}

	public Booking getBooking(String reference) {
		for (ActivityOffer offer : getActivityOfferSet()) {
			Booking booking = offer.getBooking(reference);
			if (booking != null) {
				return booking;
			}
		}
		return null;
	}

	public Set<ActivityOffer> getOffers() {
		return getActivityOfferSet();
	}

}