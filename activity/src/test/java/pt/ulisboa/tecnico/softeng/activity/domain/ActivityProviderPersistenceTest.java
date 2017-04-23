package pt.ulisboa.tecnico.softeng.activity.domain;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.Atomic.TxMode;


public class ActivityProviderPersistenceTest {

	private static final String CODE = "codigo";
	private static final String PROVIDER_NAME = "provider";
	

	ActivityProvider provider;
	
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {	
		provider = new ActivityProvider(CODE,PROVIDER_NAME);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		assertEquals(1, FenixFramework.getDomainRoot().getActivityProviderSet().size());

		List<ActivityProvider> providers = new ArrayList<>(FenixFramework.getDomainRoot().getActivityProviderSet());
		provider = providers.get(0);
		assertEquals(CODE, provider.getCode());
		assertEquals(PROVIDER_NAME, provider.getName());

	}

   @After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
	   for (ActivityProvider pro : FenixFramework.getDomainRoot().getActivityProviderSet()) {
		   for(Activity act : pro.getActivitySet()){
			   act.delete();
		   }
			pro.delete();
		}

   }

}
