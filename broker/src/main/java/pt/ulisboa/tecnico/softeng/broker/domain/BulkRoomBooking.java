package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BulkRoomBooking {
	private final int MAX_HOTEL_EXCEPTIONS = 3;
	private final int MAX_REMOTE_ERRORS = 10;
	
	private final Set<String> references = new HashSet<>();
	private final int number;
	private final LocalDate arrival;
	private final LocalDate departure;

	private static boolean cancelled = false;
	private static int numberOfHotelErrors = 0;
	private static int numberOfRemoteErrors = 0;
	
	public BulkRoomBooking(int number, LocalDate arrival, LocalDate departure) {
		this.number = number;
		this.arrival = arrival;
		this.departure = departure;
	}

	public Set<String> getReferences() {
		return this.references;
	}

	public int getNumber() {
		return this.number;
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}

	public LocalDate getArrival() {
		return this.arrival;
	}

	public LocalDate getDeparture() {
		return this.departure;
	}
	
	int getNumOfRemoteErrors() {
		return this.numberOfRemoteErrors;
	}
	void incNumOfRemoteErrors() {
		this.numberOfRemoteErrors++;
	}
	void resetNumOfRemoteErrors() {
		this.numberOfRemoteErrors = 0;
	}
	int getNumOfHotelErrors() {
		return this.numberOfHotelErrors;
	}
	void incNumOfHotelErrors() {
		this.numberOfHotelErrors++;
	}
	void resetNumOfHotelErrors() {
		this.numberOfHotelErrors = 0;
	}
	
	public void processBooking() {
		if (this.cancelled) {
			return;
		}
		try {
			this.references.addAll(HotelInterface.bulkBooking(this.number, this.arrival, this.departure));
			this.resetNumOfHotelErrors();
			this.resetNumOfRemoteErrors();
			return;
		} catch (HotelException he) {
			this.incNumOfHotelErrors();
			if (this.getNumOfHotelErrors() == MAX_HOTEL_EXCEPTIONS) {
				this.cancelled = true;
			}
			this.resetNumOfRemoteErrors();
			return;
		} catch (RemoteAccessException rae) {
			this.incNumOfRemoteErrors();
			if (this. getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				this.cancelled = true;
			}
			this.resetNumOfHotelErrors();
			return;
		}
	}

	public String getReference(String type) {
		if (this.cancelled) {
			return null;
		}
		for (String reference : this.references) {
			RoomBookingData data = null;
			try {
				data = HotelInterface.getRoomBookingData(reference);
				resetNumOfRemoteErrors();
			} catch (HotelException he) {
				resetNumOfRemoteErrors();
			} catch (RemoteAccessException rae) {
				incNumOfRemoteErrors();
				if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
					this.cancelled = true;
				}
			}

			if (data != null && data.getRoomType().equals(type)) {
				this.references.remove(reference);
				return reference;
			}
		}
		return null;
	}
}
