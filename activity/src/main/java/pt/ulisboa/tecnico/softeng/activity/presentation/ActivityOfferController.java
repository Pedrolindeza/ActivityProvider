package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData.CopyDepth;


@Controller
@RequestMapping(value = "/providers/{providerCode}/activities/{activityCode}/offers")

public class ActivityOfferController {

	private static Logger logger = LoggerFactory.getLogger(ActivityOfferController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String activityForm(Model model, @PathVariable String providerCode, @PathVariable String activityCode) {
		
		logger.info("offerForm code:{}, activityCode:{}", providerCode, activityCode);
		ActivityProviderData activityProviderData = ActivityInterface.getActivityProviderDataByCode(providerCode, CopyDepth.OFFER);
		ActivityData activityData = ActivityInterface.getActivityDataByCode(activityCode);
		
		
		if( activityProviderData == null){
			model.addAttribute("error", "Error: it does not exist a provider with the code " + providerCode);
			model.addAttribute("provider", new ActivityProviderData());
			model.addAttribute("providers", ActivityInterface.getActivityProviders());
			return "providers";
			
		}
		else if( activityData == null){
			model.addAttribute("error", "Error: it does not exist an activity with the code" + activityCode);
			model.addAttribute("activity", new ActivityData());
			model.addAttribute("activities", ActivityInterface.getActivityDatasByProvider(providerCode));
			return "activities";
			
		}
		else{
			model.addAttribute("offer", new ActivityOfferData());
			model.addAttribute("activity",activityData);
			model.addAttribute("provider", activityProviderData);
			return "offers";
		}
	
	}
	

	@RequestMapping(method = RequestMethod.POST)
	public String activitySubmit(Model model, @PathVariable String activityProviderCode, @PathVariable String activityCode, @ModelAttribute ActivityOfferData activityOfferData) {
		logger.info("activitySubmit activityCode:{} activity:{}, activityProvider:{} ,begin:{}, end:{}", activityOfferData.getActivity().getCode(), activityOfferData.getActivity(), activityOfferData.getActivity().getActivityProvider(), activityOfferData.getBegin(), activityOfferData.getEnd());

		
		try {
			ActivityInterface.createActivityOffer(activityOfferData);
		} 

		catch (ActivityException e) {
			model.addAttribute("error", "Error: it was not possible to create the activity");
			model.addAttribute("offer", activityOfferData);
			model.addAttribute("activities", ActivityInterface.getActivityDataByCode(activityCode));
			model.addAttribute("providers", ActivityInterface.getActivityProviderDataByCode(activityProviderCode,CopyDepth.OFFER));
			return "offers";
		}

		return "redirect:/providers/"+ activityProviderCode +"/activities/" + activityCode + "/offers";
	}

}