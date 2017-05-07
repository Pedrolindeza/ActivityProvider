package pt.ulisboa.tecnico.softeng.bank.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pt.ulisboa.tecnico.softeng.bank.services.local.BankInterface;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.AccountData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;


@Controller
@RequestMapping(value = "/bank/{bankCode}/client/{clientID}/account/{accountIBAN}/makeOperation")
public class MakeOperationController {
	
	private static Logger logger = LoggerFactory.getLogger(MakeOperationController.class);

	public String showMakeOperation (Model model, @PathVariable String bankCode, @PathVariable String clientID, @PathVariable String accountIBAN) {
		logger.info("showMakeOperation id:{} iban:{}", clientID, accountIBAN);

		BankData bankData = BankInterface.getBankDataByCode(bankCode);
		ClientData clientData = BankInterface.getClientDataByID(bankCode, clientID);
		AccountData accountData = BankInterface.getAccountDataByIBAN(bankCode, clientID);

		if (bankData == null) {
			model.addAttribute("error", "Error: it does not exist a bank with the code " + bankCode);
			model.addAttribute("bank", new BankData());
			model.addAttribute("banks", BankInterface.getBanks());
			return "banks";
		}
		else if (clientData == null) {
			model.addAttribute("error", "Error: it does not exist a client with the code " + clientID);
			model.addAttribute("client", new ClientData());
			model.addAttribute("clients", BankInterface.getClients());
			return "clients";
		}
		else if (accountData == null) {
			model.addAttribute("error", "Error: it does not exist an account with the code " + clientID);
			model.addAttribute("account", new ClientData());
			model.addAttribute("accounts", BankInterface.getAccounts());
			return "clients";
		}else {
			model.addAttribute("operation", new BankOperationData());
			model.addAttribute("bank", bankData);
			model.addAttribute("client", clientData);
			model.addAttribute("account", accountData);
			return "accounts";
		}
	}
	
	/*@RequestMapping(method = RequestMethod.POST)
	public String submitOperation(Model model, @PathVariable String bankCode, @ModelAttribute BankOperationData operationData) {
		logger.info("submitoperation bankCode:{}, reference:{}, type:{}, iban:{}, value:{}, time:{}", bankCode, operationData.getReference(),
				operationData.getType(), operationData.getIban(), operationData.getValue(), operationData.getTime());

		try {
			BankInterface.createOperation(bankCode, operationData);
		} catch (BankException he) {
			model.addAttribute("error", "Error: it was not possible to create the operation");
			model.addAttribute("operation", operationData);
			model.addAttribute("bank", BankInterface.getBankDataByCode(bankCode));
			return "operations";
		}

		return "redirect:/bank/" + bankCode + "/operations";
	}*/
}
