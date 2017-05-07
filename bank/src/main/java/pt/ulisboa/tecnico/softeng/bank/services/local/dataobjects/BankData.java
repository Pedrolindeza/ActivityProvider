package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;

public class BankData {
	
	public static enum CopyDepth {
		CLIENT
	};
	
	private List<ClientData> clients = new ArrayList<>();

	private String name;
	private String code; 
	
	public BankData(){
		
	}
	
	public BankData(Bank bank) {
		this.name = bank.getName();
		this.code = bank.getCode();

	}
	
	public BankData(Bank bank, CopyDepth depth) {
		this.name = bank.getName();
		this.code = bank.getCode();
		switch (depth) {
		//TODO add more
		case CLIENT:
			for (Client client: bank.getClientSet()) {
				this.clients.add(new ClientData(client));
			}
			break;
		}
		
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public List<ClientData> getClients(){
		return clients;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setCode(String code){
		this.code = code; 
	}

	public void addClient(ClientData client){
		clients.add(client);
	}
}