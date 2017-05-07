package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import org.joda.time.DateTime;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type;


public class BankOperationData {
	private String reference;
	private Type type;
	private String iban;
	private int value;
	private DateTime time;
	private Account account;

	public BankOperationData() {
	}

	public BankOperationData(Operation operation) {
		this.reference = operation.getReference();
		this.type = operation.getType();
		this.iban = operation.getAccount().getIBAN();
		this.value = operation.getValue();
		this.time = operation.getTime();
		this.account = operation.getAccount();
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getIban() {
		return this.iban;
	}

	public Account getAccount() {
		return this.account;
	}
	
	public void setAccount(Account account) {
		this.account = account;
	}
	
	public void setIban(String iban) {
		this.iban = iban;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public DateTime getTime() {
		return this.time;
	}

	public void setTime(DateTime time) {
		this.time = time;
	}
}
