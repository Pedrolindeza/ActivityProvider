package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankHasAccountMethodTest {
	Bank bank;
	Client client;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		this.client = new Client(this.bank, "António");
		
	}

	@Test
	public void success() {
		Account account = new Account(this.bank, this.client);
		Account result = this.bank.getAccount(account.getIBAN());
		Assert.assertEquals(account, result);
			
	}
	
	@Test
	public void zeroAccounts(){
		exception.expect(BankException.class);
		this.bank.getAccount("Jesus is the bread.");
	}
	
	@Test
	public void IBANnotFound(){
		exception.expect(BankException.class);
		Account account1 = new Account(this.bank, this.client);
		Account account2 = new Account(this.bank, this.client);
		Account account3 = new Account(this.bank, this.client);
		Account account4 = new Account(this.bank, this.client);
		this.bank.addAccount(account1);
		this.bank.addAccount(account2);
		this.bank.addAccount(account3);
		this.bank.addAccount(account4);
		
		String IBAN = account1.getIBAN() + "666";
		this.bank.getAccount(IBAN);
	}

	@Test
	public void invalidArg(){
		
		exception.expect(BankException.class);
		
		Bank Banif = new Bank("Banif", "BA69");
		Account account = new Account(Banif, this.client);
		
		this.bank.getAccount( account.getIBAN() );
		
	}
	
	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
