package pt.ulisboa.tecnico.softeng.bank.domain;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;


public class AccountContructorMethodTest {
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

		Assert.assertEquals(this.bank, account.getBank());
		Assert.assertTrue(account.getIBAN().startsWith(this.bank.getCode()));
		Assert.assertEquals(this.client, account.getClient());
		Assert.assertEquals(0, account.getBalance());
		Assert.assertEquals(1, this.bank.getNumberOfAccounts());
		Assert.assertTrue(this.bank.hasClient(this.client));
	}
	

	@Test
 	public void accountNull(){
 		exception.expect(BankException.class);
 		new Account(null,null);
 	}
	
	
	@Test
 	public void diffBank(){
 		exception.expect(BankException.class);
 		Bank bank2 = new Bank("Money1", "BK02");
 		new Account(bank2,this.client);
 	}
	
	
	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
