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
	public void setUp() {}

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
	public void nullName(){
		new Bank(null,"BK06");
		
	}
	
	@Test(expected = BankException.class)
	public void nullCode(){
		new Bank("MoneyMoney",null);
		
	}
	
	@Test(expected = BankException.class)
	public void emptyName(){
		new Bank("","BK01");
		
	}
	
	@Test(expected = BankException.class)
	public void blankName(){
		new Bank("  ","BK01");
		
	}

	@Test(expected = BankException.class)
	public void emptyCode(){
		new Bank("Money33","");
		
	}
	
	@Test(expected = BankException.class)
	public void blankCode(){
		new Bank("Money33","  ");
		
	}
	
	@Test(expected = BankException.class)
	public void sameCode(){
		new Bank("Money8", "BK03");
		new Bank("Money2","BK03");
		
	}
	
	@Test(expected = BankException.class)
	public void bigCode(){
		new Bank("Money3","BK012");
		
	}

	@Test(expected = BankException.class)
	public void smallCode(){
		new Bank("Money4","BK2");
		
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}
}
