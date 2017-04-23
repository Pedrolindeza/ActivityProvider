package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@SuppressWarnings("unused")
public class BulkRoomBooking extends BulkRoomBooking_Base{
	public static final int MAX_HOTEL_EXCEPTIONS = 3;
	public static final int MAX_REMOTE_ERRORS = 10;



	public BulkRoomBooking(int number, LocalDate arrival, LocalDate departure) {
		setNumber (number);
		setArrival (arrival);
		setDeparture (departure);
		
		setCancelled (false);
		
	}



	public void processBooking() {
		if (this.getCancelled()) {
			return;
		}

		try {
			
			for (String s : HotelInterface.bulkBooking(this.getNumber(), this.getArrival(), this.getDeparture()) ) {
				Reference ref = new Reference(s);
				this.addReference( ref );
			}
			
			
			this.setNumberOfHotelExceptions(0);
			this.setNumberOfRemoteErrors(0);
			return;
		} catch (HotelException he) {
			this.setNumberOfHotelExceptions(this.getNumberOfHotelExceptions() + 1);
			if (this.getNumberOfHotelExceptions() == MAX_HOTEL_EXCEPTIONS) {
				this.setCancelled(true);
			}
			this.setNumberOfRemoteErrors(0);
			return;
			
		} catch (RemoteAccessException rae) {
			
			this.setNumberOfRemoteErrors(this.getNumberOfRemoteErrors()+1);
			
			if (this.getNumberOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				this.setCancelled(true);
			}
			this.setNumberOfHotelExceptions(0);
			return;
		}
	}

	public String getReference(String type) {
		if (this.getCancelled()) {
			return null;
		}

		for (Reference ref : this.getReferenceSet()) {
			RoomBookingData data = null;
			try {
				data = HotelInterface.getRoomBookingData(ref.getReference());
				this.setNumberOfRemoteErrors(0);
			} catch (HotelException he) {
				this.setNumberOfRemoteErrors(0);
			} catch (RemoteAccessException rae) {
				this.setNumberOfRemoteErrors(this.getNumberOfRemoteErrors()+1);
				if (this.getNumberOfRemoteErrors() == MAX_REMOTE_ERRORS) {
					this.setCancelled(true);
				}
			}

			if (data != null && data.getRoomType().equals(type)) {
				this.removeReference(ref);
				return ref.getReference();
			}
		}
		return null;
	}
}
