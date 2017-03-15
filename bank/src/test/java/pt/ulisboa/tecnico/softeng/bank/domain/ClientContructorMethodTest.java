package pt.ulisboa.tecnico.softeng.bank.domain;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;


public class ClientContructorMethodTest {
	Bank bank;

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
	}

	@Test
	public void success() {
		Client client = new Client(this.bank, "António");

		Assert.assertEquals("António", client.getName());
		Assert.assertTrue(client.getID().length() >= 1);
		Assert.assertTrue(this.bank.hasClient(client));
	}

	
	@Test
 	public void bankNull(){
 		exception.expect(BankException.class);
 		new Client(null,"Madalena");
 	}
	
	@Test
 	public void nameNull(){
 		exception.expect(BankException.class);
 		new Client(this.bank,null);
 	}

 	@Test(expected = BankException.class)
	public void emptyName(){
		new Client(this.bank,"");
		
	}
	
	@Test(expected = BankException.class)
	public void blankName(){
		new Client(this.bank,"   ");
		
	}
	
	
	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
