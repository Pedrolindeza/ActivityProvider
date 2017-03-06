package pt.ulisboa.tecnico.softeng.bank.domain;


import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;



public class BankConstructorTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() {

	}

	@Test
	public void success() {
		Bank bank = new Bank("Money", "BK01");

		Assert.assertEquals("Money", bank.getName());
		Assert.assertEquals("BK01", bank.getCode());
		Assert.assertEquals(1, Bank.banks.size());
		Assert.assertEquals(0, bank.getNumberOfAccounts());
		Assert.assertEquals(0, bank.getNumberOfClients());
	}

	@Test(expected = BankException.class)
	public void nullString(){
		Bank bank = new Bank(null,null);
		
	}
	
	@Test(expected = BankException.class)
	public void sameCode(){
		new Bank("Money2","BK01");
		
	}
	
	@Test(expected = BankException.class)
	public void dimCode(){
		new Bank("Money3","BK012");
		
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}
}
