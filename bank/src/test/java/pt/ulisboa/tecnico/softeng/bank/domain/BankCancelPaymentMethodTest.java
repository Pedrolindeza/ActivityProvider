package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

@SuppressWarnings("unused")
public class BankCancelPaymentMethodTest {
	private Bank bank;
	private Client client;
	private Account account;
	private Operation operation;
	private String IBAN;
	
	@Before
	public void setUp() {
		this.bank = new Bank("SLBenfica", "SLB1");
		this.client = new Client(this.bank, "Jonas Pistolas");
		this.account = new Account(this.bank, this.client);
		this.IBAN = this.account.getIBAN();
		this.account.deposit(100);
	}
	
	@Test
	public void success() {
		
		String ref = Bank.processPayment(this.account.getIBAN(), 10);
		Bank.cancelPayment(ref);
		Assert.assertEquals(100, this.account.getBalance());
	}
	
	@Test(expected = BankException.class)
	public void unknownReference(){
		Bank.cancelPayment("Sempre quis ser toureiro.");
	}
	
	@Test(expected = BankException.class)
	public void nullReference() {
		Bank.cancelPayment(null);
	}

	@Test(expected = BankException.class)
	public void emptyReference() {
		Bank.cancelPayment("");
	}

	@Test(expected = BankException.class)
	public void blankReference() {
		Bank.cancelPayment(" ");
	}

	@Test(expected = BankException.class)
	public void endLineReference() {
		Bank.cancelPayment("\0");
	}
	
	@Test(expected = BankException.class)
	public void newLineReference() {
		Bank.cancelPayment("\n");
	}
	
	@After
	public void tearDown() {
		Bank.banks.clear();
	}
	
	
}
