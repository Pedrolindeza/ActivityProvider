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
			System.out.println("Reservation: reference-" + operation.getReference() + " type:" + 
					operation.getType() + " IBAN:" + operation.getIban() + " value:" + 
					operation.getValue() + " time:" + operation.getTime() + " .");
		} catch (BankException be) {
			this.incNumOfRemoteErrors(); 
			
			if (numOfRemoteErrors == 5) {
				adventure.setState(State.UNDO);
			}
			return;
		} catch (RemoteAccessException rae) {
			this.incNumOfRemoteErrors(); 
			
			if (numOfRemoteErrors == 20) {
				adventure.setState(State.UNDO);
			}
			return;
		}
		this.resetNumOfRemoteErrors();
	
		ActivityReservationData reservation;
		try {
			reservation = ActivityInterface.getActivityReservationData(adventure.getActivityConfirmation());
			System.out.println("Reservation: reference-" + reservation.getReference() + " cancellation:" + 
					reservation.getCancellation() + " name:" + reservation.getName() + " code:" + 
					reservation.getCode() + " begin:" + reservation.getBegin() + " end:" + 
					reservation.getEnd() + " cancellationDate:" + reservation.getCancellationDate() +" .");
		} catch (ActivityException ae) {
			adventure.setState(State.UNDO);
			return;
		} catch (RemoteAccessException rae) {
			this.incNumOfRemoteErrors(); 
			
			if (numOfRemoteErrors == 20) {
				adventure.setState(State.UNDO);
			}
			return;
		}
		this.resetNumOfRemoteErrors();
	
		if (adventure.getRoomConfirmation() != null) {
			RoomBookingData booking;
			try {
				booking = HotelInterface.getRoomBookingData(adventure.getRoomConfirmation());
				System.out.println("Booking: reference-" + booking.getReference() + " cancellation:" + 
						booking.getCancellation() + " hotelName:" + booking.getHotelName() + " hotelCode:" + 
						booking.getHotelCode() + " roomNumber:" + booking.getRoomNumber() + 
						" roomType:" + booking.getRoomType() + " arrival:" + booking.getArrival() + 
						" departure:" + booking.getDeparture() + " cancellationDate:" + booking.getCancellationDate() + " .");
			} catch (HotelException he) {
				adventure.setState(State.UNDO);
				return;
			} catch (RemoteAccessException rae) {
				this.incNumOfRemoteErrors(); 
				
				if (numOfRemoteErrors == 20) {
					adventure.setState(State.UNDO);
				}
				return;
			}
			this.resetNumOfRemoteErrors();
		}
		
		
	
		
		
	}
}
