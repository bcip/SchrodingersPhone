package com.github.bcip.schrodingersphone;

import java.io.Serializable;
import java.util.Date;

public class Record implements Serializable {
	private static final long serialVersionUID = -5313663493179017564L;

	public Record(String username, Date time, Feature feature) {
		this.username = username;
		this.time = time;
		this.feature = feature;
	}

	public Record(String username, Date time, String activity) {
		this.username = username;
		this.time = time;
		this.activity = activity;
	}

	public String getUsername() {
		return username;
	}

	public Date getTime() {
		return time;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getActivity() {
		return activity;
	}

	private String username = null;
	private Date time = null;
	private Feature feature = null;
	private String activity = null;
}
