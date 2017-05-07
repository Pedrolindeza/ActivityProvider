package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData.CopyDepth;


@Controller
@RequestMapping(value = "/providers/{providerCode}/activity/")

public class ActivityController {

	private static Logger logger = LoggerFactory.getLogger(ActivityController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String activityForm(Model model, @PathVariable String providerCode) {
		
		logger.info("activityForm code:{}", providerCode);
		ActivityProviderData providerData = ActivityInterface.getActivityProviderDataByCode(providerCode, CopyDepth.ACTIVITY);
		
		if( providerData == null){
			model.addAttribute("error", "Error: it does not exist an activity provider  with the code" + providerCode);
			model.addAttribute("activityProvider", new ActivityProviderData());
			model.addAttribute("activityProviders", ActivityInterface.getActivityProviders());
			return "activityProviders";
			
		}
		else{
			model.addAttribute("activity", new ActivityData());
			model.addAttribute("activityProvider",providerData);
			return "activities";
		}
	
	}
	

	@RequestMapping(method = RequestMethod.POST)
	public String activitySubmit(Model model, @PathVariable String providerCode, @ModelAttribute ActivityData activityData) {
		logger.info("activitySubmit providerCode:{} provider:{}, name:{}, minAge:{}, maxAge:{}, capacity:{}", providerCode, activityData.getActivityProvider(), activityData.getName(), activityData.getMinAge(), activityData.getMaxAge(), activityData.getCapacity());

		
		try {
			ActivityInterface.createActivity(providerCode, activityData);
		} 

		catch (ActivityException e) {
			model.addAttribute("error", "Error: it was not possible to create the activity");
			model.addAttribute("activity", activityData);
			model.addAttribute("activityProvider", ActivityInterface.getActivityProviderDataByCode(providerCode,CopyDepth.ACTIVITY));
			return "activities";
		}

		return "redirect:/providers/" + providerCode + "/activities";
	}

}
