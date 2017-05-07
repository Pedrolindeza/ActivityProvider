package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;


public class ActivityProviderData {

	public static enum CopyDepth{
		SHALLOW, ACTIVITY, OFFER
	};
	
	private String name;
	private String code;
	private List<ActivityData> activities = new ArrayList<>();

	public ActivityProviderData() {
	}

	public ActivityProviderData(ActivityProvider activityProvider, CopyDepth depth) {
		this.name = activityProvider.getName();
		this.code = activityProvider.getCode();
		
		switch(depth){
		case SHALLOW:
			break;
		case ACTIVITY:
			for (Activity activity : activityProvider.getActivitySet()) {
				this.activities.add(new ActivityData(activity));
			}
			break;
		case OFFER:
			break; //TO DO
		}

	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<ActivityData> getActivities() {
		return this.activities;
	}

	public void setActivities(List<ActivityData> activities) {
		this.activities = activities;
	}

}
