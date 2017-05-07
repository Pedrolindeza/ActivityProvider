package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;


public class ActivityOfferData {

	private Activity activity;
	private LocalDate begin;
	private LocalDate end;

	public ActivityOfferData() {
	}

	public ActivityOfferData(ActivityOffer activityOffer) {
		this.activity = activityOffer.getActivity();
		this.begin = activityOffer.getBegin();
		this.end = activityOffer.getEnd();
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public LocalDate getBegin() {
		return begin;
	}

	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}
	
}
