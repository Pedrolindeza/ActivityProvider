package pt.ulisboa.tecnico.softeng.bank.presentation;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.bank.services.local.BankInterface;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;

@Controller
@RequestMapping(value = "/bank/{bankCode}/operations")
public class OperationController {

	private static Logger logger = LoggerFactory.getLogger(OperationController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showClients(Model model, @PathVariable String bankCode) {
		logger.info("showOperations code:{}", bankCode);

		BankData bankData = BankInterface.getBankDataByCode(bankCode);

		if (bankData == null) {
			model.addAttribute("error", "Error: it does not exist a bank with the code " + bankCode);
			model.addAttribute("bank", new BankData());
			model.addAttribute("banks", BankInterface.getBanks());
			return "banks";
		} else {
			model.addAttribute("operation", new BankOperationData());
			model.addAttribute("bank", bankData);
			return "operations";
		}
	}	
	
	@RequestMapping(method = RequestMethod.POST)
	public String submitOperation(Model model, @PathVariable String bankCode, @ModelAttribute BankOperationData operationData) {
		logger.info("submitoperation bankCode:{}, reference:{}, type:{}, iban:{}, value:{}, time:{}", bankCode, operationData.getReference(),
				operationData.getType(), operationData.getIban(), operationData.getValue(), operationData.getTime());

		try {
			BankInterface.createOperation(bankCode, operationData);
		} catch (BankException he) {
			model.addAttribute("error", "Error: it was not possible to create the operation");
			model.addAttribute("client", operationData);
			model.addAttribute("bank", BankInterface.getBankDataByCode(bankCode));
			return "clients";
		}

		return "redirect:/bank/" + bankCode + "/clients";
	}
}
