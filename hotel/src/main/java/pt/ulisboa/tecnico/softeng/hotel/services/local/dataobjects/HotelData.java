package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;

public class HotelData {
	private String name;
	private String code; 
	
	public HotelData(){
		
	}
	
	public HotelData(Hotel hotel) {
		this.name = hotel.getName();
		this.code = hotel.getCode();

	}
	
	public String getName(){
		return this.name;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setCode(String code){
		this.code = code; 
	}
	
}
