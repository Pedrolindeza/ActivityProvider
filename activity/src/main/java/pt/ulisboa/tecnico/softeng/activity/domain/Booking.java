package pt.ulisboa.tecnico.softeng.activity.domain;

import java.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class Booking {
	private static int counter = 0;
	private final String reference;
	private String cancellationReference= null;
	private LocalDate cancelationDate;

	public Booking(ActivityProvider provider, ActivityOffer offer) {
		checkArguments(provider, offer);

		this.reference = provider.getCode() + Integer.toString(++Booking.counter);

		offer.addBooking(this);
	}

	private void checkArguments(ActivityProvider provider, ActivityOffer offer) {
		if (provider == null || offer == null) {
			throw new ActivityException();
		}

	}

	public String getReference() {
		return this.reference;
	}
	
	public String getCancellationReference(){
		return this.cancellationReference;
	}
	
	public String setCancellationReference(){
		if (this.cancellationReference!=null)
			throw new ActivityException("Booking already cancelled");
		else {
			this.cancelationDate=LocalDate.now();
			this.cancellationReference= "Reference: " + this.getReference() + "cancelled on " + this.cancelationDate;
		}
			return this.cancellationReference;
		
	}
}
