package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;

public class ClientData {
	private String name;
	private String id;
	private Bank bank; 
	
	public ClientData(){
		
	}
	
	public ClientData(Client client) {
		this.name = client.getName();
		this.id = client.getID();
		this.bank = client.getBank();

	}
	
	public String getName(){
		return this.name;
	}
	
	public String getID(){
		return this.id;
	}

	public Bank getBank(){
		return this.bank;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setID(String id){
		this.id = id; 
	}
	
	public void setBank(Bank bank){
		this.bank = bank; 
	}
}
