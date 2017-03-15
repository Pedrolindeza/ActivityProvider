package pt.ulisboa.tecnico.softeng.activity.domain;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class Booking {
	private static int counter = 0;

	private final String reference;

	public Booking(ActivityProvider provider, ActivityOffer offer) {
		if(provider == null || offer == null)
			throw new ActivityException();
		try {	
			this.reference = provider.getCode() + Integer.toString(++Booking.counter);
			offer.addBooking(this);
		}
		catch (ActivityException e) {
			throw new ActivityException("Can't create the booking, offer's capacity is full");
		}
	}

	public String getReference() {
		return this.reference;
	}
}
