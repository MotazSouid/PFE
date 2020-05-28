package com.alfresco.api.example.model;

import com.google.api.client.util.Key;


public class Comment {

	@Key
    public String id;
		
	@Key
    public String title;

	@Key
	public String content;

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

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

}
