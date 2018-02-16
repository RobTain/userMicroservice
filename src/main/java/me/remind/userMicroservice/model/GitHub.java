package me.remind.userMicroservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GitHub {

	private String name;
	private String language;
	
	@JsonCreator
	public GitHub(@JsonProperty("name") String name, @JsonProperty("language") String language) {
		this.name = name;
		this.language = language;
	}

	public String toString() {
		return name + " (" + language + ")";
	}

}
