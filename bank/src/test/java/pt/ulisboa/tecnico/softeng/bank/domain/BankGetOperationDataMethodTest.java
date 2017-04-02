package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;


public class BankGetOperationDataMethodTest {
	private Bank bank;
	private Client client;
	private Account account;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		this.client = new Client(this.bank, "António");
		this.account = new Account(this.bank, this.client);
	}

	@Test
	public void success() {
		String reference = account.deposit(10);
		BankOperationData b = Bank.getOperationData(reference);

		Assert.assertEquals(b.getReference(),reference);
		Assert.assertEquals(b.getType(),"deposit");
		Assert.assertEquals(b.getTime(),this.bank.getOperation(reference).getTime());
		Assert.assertEquals(b.getValue(),this.bank.getOperation(reference).getValue());
		Assert.assertEquals(b.getIban(),this.account.getIBAN());
	}
	
	@Test
	public void success2() {
		account.deposit(10);
		String reference = account.withdraw(1);
		BankOperationData b = Bank.getOperationData(reference);

		Assert.assertEquals(b.getReference(),reference);
		Assert.assertEquals(b.getType(),"withdraw");
		Assert.assertEquals(b.getTime(),this.bank.getOperation(reference).getTime());
		Assert.assertEquals(b.getValue(),this.bank.getOperation(reference).getValue());
		Assert.assertEquals(b.getIban(),this.account.getIBAN());
	}
	
	@Test(expected = BankException.class)
	public void nullReference() {
		Bank.getOperationData(null);
	}

	@Test(expected = BankException.class)
	public void emptyReference() {
		Bank.getOperationData("");
	}

	@Test(expected = BankException.class)
	public void blankReference() {
		Bank.getOperationData(" ");
	}

	@Test(expected = BankException.class)
	public void endLineReference() {
		Bank.getOperationData("\0");
	}
	
	@Test(expected = BankException.class)
	public void newLineReference() {
		Bank.getOperationData("\n");
	}
	
	@Test(expected = BankException.class)
	public void nonExistanceReference() {
		Bank.getOperationData("lol");
	}
	

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
