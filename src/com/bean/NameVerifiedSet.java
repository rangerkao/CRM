package com.bean;

import java.util.List;

public class NameVerifiedSet {

	
	NameVerified current;
	
	List<NameVerified> history;

	public NameVerified getCurrent() {
		return current;
	}

	public void setCurrent(NameVerified current) {
		this.current = current;
	}

	public List<NameVerified> getHistory() {
		return history;
	}

	public void setHistory(List<NameVerified> history) {
		this.history = history;
	}
	
	
}
