package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type;
import pt.ist.fenixframework.FenixFramework;

public class BankPersistenceTest {
	private static final String BANK_CODE = "BK01";
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Bank bank = new Bank("Money", BANK_CODE);
		Client client = new Client(bank, "José");
		Account account = new Account(bank, client);
		Operation operation = new Operation(Type.DEPOSIT,account,2);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		
		assertEquals(1, FenixFramework.getDomainRoot().getBankSet().size());
		List<Bank> banks = new ArrayList<>(FenixFramework.getDomainRoot().getBankSet());
		Bank bank = banks.get(0);

		List<Client> clients = new ArrayList<>(bank.getClientsSet());
		Client client = clients.get(0);
		
		List<Account> accounts = new ArrayList<>(bank.getAccountSet());
		Account account = accounts.get(0);
		
		List<Operation> operations = new ArrayList<>(bank.getLogSet());
		Operation operation = operations.get(0);
		
		assertEquals(BANK_CODE, bank.getCode());
		assertEquals("Money", bank.getName());
		
		assertEquals(client.getName(), "José");
		assertEquals(client.getBank(), bank);
		
		assertEquals(account.getClient(), client);
		assertEquals(account.getBank(), bank);
		
		assertEquals(bank, operation.getBank());
		assertEquals(account, operation.getAccount());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			for (Operation operation: bank.getLogSet())
				operation.delete();
			for (Account account: bank.getAccountSet())
				account.delete();
			for (Client client: bank.getClientsSet())
				client.delete();
			bank.delete();
		}
	}

}
