package me.remind.userMicroservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	private String forename;

	private String surname;
	private String position;
	@Column(unique = true)
	private String link;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getForename() {
		return forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	
	
	
	public boolean equals(User user) {
		boolean equals = false;
		// check unique link
		if (this.getLink() != null && user.getLink() != null) {
			if (this.getLink().equals(user.getLink())) {
				System.out.println("das erste");
				equals = true;
			}
		}
		
		
		
		// same user? -> only if all user.values equal
		if (this.getForename().equals(user.getForename()) && this.getSurname().equals(user.getSurname())
				&& this.getPosition().equals(user.getPosition())) {
			System.out.println("das zweite");
			equals = true;

		}
		return equals;
	}
}
