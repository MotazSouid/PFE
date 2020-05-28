package com.alfresco.api.example.model;

import com.google.api.client.util.Key;


public class Site {

    @Key
    public String id;

    @Key
    public String title;

    @Key
    public String visibility;

    @Key
    public String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
