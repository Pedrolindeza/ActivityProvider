package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class ConfirmedState extends AdventureState{
	
	@Override
 	public State getState(){
 		return State.CONFIRMED;
 	}
	
	@Override
	public void process(Adventure adventure) {
		BankOperationData operation;
		try {
			operation = BankInterface.getOperationData(adventure.getPaymentConfirmation());
		} catch (BankException be) {
			// increment number of errors
			// if (number of errors == 5) {
			// adventure.setState(State.UNDO);
			// }
			// return;
		} catch (RemoteAccessException rae) {
			// increment number of errors
			// if (number of errors == 20) {
			// adventure.setState(State.UNDO);
			// }
			// return;
		}
		// reset number of errors
	
		ActivityReservationData reservation;
		try {
			reservation = ActivityInterface.getActivityReservationData(adventure.getActivityConfirmation());
		} catch (ActivityException ae) {
			adventure.setState(State.UNDO);
			return;
		} catch (RemoteAccessException rae) {
			// increment number of errors
			// if (number of errors == 20) {
			// adventure.setState(State.UNDO);
			// }
			// return;
		}
		// reset number of errors
	
		if (adventure.getRoomConfirmation() != null) {
			RoomBookingData booking;
			try {
				booking = HotelInterface.getRoomBookingData(adventure.getRoomConfirmation());
			} catch (HotelException he) {
				adventure.setState(State.UNDO);
				return;
			} catch (RemoteAccessException rae) {
				// increment number of errors
				// if (number of errors == 20) {
				// adventure.setState(State.UNDO);
				// }
				// return;
			}
			// reset number of errors
		}
	
		// TODO: prints the complete Adventure file, the info in operation,
		// reservation and booking
	
	}
}