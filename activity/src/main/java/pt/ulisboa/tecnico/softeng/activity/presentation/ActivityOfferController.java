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
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData.CopyDepth;


@Controller
@RequestMapping(value = "/providers/{activityCode}/activities/{activities.code}/offer")

public class ActivityOfferController {

	private static Logger logger = LoggerFactory.getLogger(ActivityOfferController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String activityForm(Model model, @PathVariable String activityCode) {
		
		logger.info("activityForm code:{}", activityCode);
		ActivityData activityData = ActivityInterface.getActivityDataByCode(activityCode);
		
		if( activityData == null){
			model.addAttribute("error", "Error: it does not exist an activity with the code" + activityCode);
			model.addAttribute("activity", new ActivityData());
			model.addAttribute("providers", ActivityInterface.getActivityProviders());
			return "providers";
			
		}
		else{
			model.addAttribute("offer", new ActivityOfferData());
			model.addAttribute("provider",activityData);
			return "offer";
		}
	
	}
	

	@RequestMapping(method = RequestMethod.POST)
	public String activitySubmit(Model model, @PathVariable String activityCode, @ModelAttribute ActivityData activityData) {
		logger.info("activitySubmit activityCode:{} provider:{}, name:{}, minAge:{}, maxAge:{}, capacity:{}", activityCode, activityData.getActivityProvider(), activityData.getName(), activityData.getMinAge(), activityData.getMaxAge(), activityData.getCapacity());

		
		try {
			ActivityInterface.createActivity(activityCode, activityData);
		} 

		catch (ActivityException e) {
			model.addAttribute("error", "Error: it was not possible to create the activity");
			model.addAttribute("activity", activityData);
			model.addAttribute("provider", ActivityInterface.getActivityProviderDataByCode(activityCode,CopyDepth.ACTIVITY));
			return "activities";
		}

		return "redirect:/providers/" + activityCode + "/activities";
	}

}