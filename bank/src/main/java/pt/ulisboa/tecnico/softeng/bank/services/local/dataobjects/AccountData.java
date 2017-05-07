package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;

public class AccountData {
	private String IBAN;
	private int balance;
	private Client client;
	private Bank bank;
	
	public AccountData(){
	}
	
	public AccountData(Account account) {
		this.IBAN = account.getIBAN();
		this.balance = account.getBalance();
		this.client = account.getClient();
		this.bank = account.getBank();
	}
	
	public String getIBAN() { return this.IBAN; }
	public void setIBAN(String IBAN) { this.IBAN = IBAN; }
	public int getBalance() { return this.balance; }
	public void setBalance(int balance) { this.balance = balance; }
	public Client getClient() { return this.client; }
	public void setClient(Client client) { this.client = client; }
	public Bank getBank() { return this.bank; }
	public void setBank(Bank bank) { this.bank = bank; }
}
