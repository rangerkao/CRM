package com.bean;

import java.util.List;

public class Detail {
	
	
	String gprsStatus;
	List<AddonService> addonservices;
	Application application;

	public String getGprsStatus() {
		return gprsStatus;
	}

	public void setGprsStatus(String gprsStatus) {
		this.gprsStatus = gprsStatus;
	}

	public List<AddonService> getAddonservices() {
		return addonservices;
	}

	public void setAddonservices(List<AddonService> addonservices) {
		this.addonservices = addonservices;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

}
