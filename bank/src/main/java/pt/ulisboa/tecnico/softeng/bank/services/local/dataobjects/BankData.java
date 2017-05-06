package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.bank.domain.Bank;

public class BankData {
	private String name;
	private String code; 
	
	public BankData(){
		
	}
	
	public BankData(Bank bank) {
		this.name = bank.getName();
		this.code = bank.getCode();

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