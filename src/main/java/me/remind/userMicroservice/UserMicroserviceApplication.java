package me.remind.userMicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class UserMicroserviceApplication {

/*
 * TODO: Github Service bauen -> Für jede Entity mit Aufgabe gibt es einen eigenen Service (mit Auth2)
 * TODO: Service benötigt keine SQL Anfragen - baut Spring automatisch! Nachschauen!!!
 * TODO: Tests nicht nach Reihenfolge, sondern abgeschlossen 
 */

	public static void main(String[] args) {
		SpringApplication.run(UserMicroserviceApplication.class, args);
	}
}
