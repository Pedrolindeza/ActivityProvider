package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class UndoState extends AdventureState {
	
	@Override
	public State getState(){
		return State.UNDO;
	}

	@Override
	public void process(Adventure adventure) {
		if (adventure.cancelPayment()) {
			try {
				adventure.setPaymentCancellation(BankInterface.cancelPayment(adventure.getPaymentConfirmation()));
			} catch (HotelException | RemoteAccessException ex) {
				return;
			}
		}
	
		if (adventure.cancelActivity()) {
			try { 
				adventure.setActivityCancellation(ActivityInterface.cancelReservation(adventure.getActivityConfirmation()));
			} catch (HotelException | RemoteAccessException ex) {
				return;
			}
		}
	
		if (adventure.cancelRoom()) {
			try {
				adventure.setRoomCancellation(HotelInterface.cancelBooking(adventure.getRoomConfirmation()));
			} catch (HotelException | RemoteAccessException ex) {
				return;
			}
		}
		 
		if (!adventure.cancelPayment() && !adventure.cancelActivity() && !adventure.cancelRoom()) {
			adventure.setState(State.CANCELLED);
		}
	
	}
}
