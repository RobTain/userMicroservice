package me.remind.userMicroservice;

import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.junit.Assert.assertEquals;

import org.h2.engine.SysProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestBody;

import me.remind.userMicroservice.model.User;

/**
 * Testing the integration of the userMicroservice application. The tests
 * includes creation/modification/deletion of valid and not valid users and the
 * successful data transmissions from an external link into this application.
 * 
 * The order of the methods are very important. Therefore the camel case pattern
 * were not correctly followed.
 * 
 * @author Robert Koenig
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserMicroserviceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserMicroserviceApplicationTests {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final TestRestTemplate restTemplate = new TestRestTemplate();
	private final HttpHeaders headers = new HttpHeaders();
	

	@LocalServerPort
	private int port;

	/**
	 * Check for correct initialization of the spring boot application.
	 * 
	 * Initial State:
	 *  
	 * + User Table added 
	 * + /users/ will return a empty list
	 * 
	 * Expected HTTP Header: 200 (OK) 
	 * Expected String representation: "[]"
	 */
	@Test
	public void test01_checkInitialState() {
		logger.info("TEST> Check initial state of the software!");

		// build web call
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		// call site
		ResponseEntity<String> response = restTemplate.exchange(localhostLink(), HttpMethod.GET, entity, String.class);

		// tests
		int expectedHeader = 200;
		String expectedStringRepresentation = "[]";

		assertEquals(expectedHeader, response.getStatusCodeValue());
		assertEquals(expectedStringRepresentation, response.getBody());
	}


	/**
	 * Create an valid user. A valid user has an unique github-link and can not have
	 * the same attributes as an other user entity. Afterwards check for a String
	 * representation of the created user
	 * 
	 * Expected HTTP Header: 201 (Created) 
	 * Expected String representation:
	 * "forename":"Robert","surname":"Koenig","position":"Java Developer",
	 * "link":"https://github.com/RobTain
	 */
	@Test
	public void test02_createValidUser() {
		logger.info("TEST> Create a valid user!");
		// create user
		User user = new User();
		user.setForename("Robert");
		user.setSurname("Koenig");
		user.setPosition("Java Developer");
		user.setLink("https://github.com/RobTain");

		// build web call
		HttpEntity<User> entity = new HttpEntity<>(user, headers);

		// call site
		ResponseEntity<String> response = restTemplate.exchange(localhostLink(), HttpMethod.POST, entity, String.class);

		// test
		int expectedHeader = 201;
		assertEquals(expectedHeader, response.getStatusCodeValue());

		// check String representation
		response = restTemplate.exchange(localhostLink(), HttpMethod.GET, entity, String.class);

		// test
		String expectedStringRepresentation = "\"forename\":\"" + user.getForename() + "\",\"surname\":\""
				+ user.getSurname() + "\",\"position\":\"" + user.getPosition() + "\",\"link\":\"" + user.getLink();
		assertThat(response.getBody()).contains(expectedStringRepresentation);

	}

	/**
	 * Create a new user with same input as a user entity in DB
	 * 
	 * Expected HTTP Header 409 (Conflict)
	 */
	@Test
	public void test03_createDublicate() {
		logger.info("TEST> Create a new user with same input as a user entity in DB!");
		// create user
		User user = new User();
		user.setForename("Robert");
		user.setSurname("Koenig");
		user.setPosition("Java Developer");
		user.setLink("https://github.com/RobTain");

		// build web call
		HttpEntity<User> entity = new HttpEntity<>(user, headers);

		// call site
		ResponseEntity<String> response = restTemplate.exchange(localhostLink(), HttpMethod.POST, entity, String.class);

		// test
		int expectedHeader = 409;
		assertEquals(expectedHeader, response.getStatusCodeValue());
	}

	/**
	 * Create a new user with same input except the link
	 * 
	 * Expected HTTP Header 409 (Conflict)
	 */
	@Test
	public void test04_createSameUserWithExistingData() {
		logger.info("TEST> Create a new user with same input except the link!");
		// create user
		User user = new User();
		user.setForename("Robert");
		user.setSurname("Koenig");
		user.setPosition("Java Developer");
		user.setLink("https://www.google.de");

		// build web call
		HttpEntity<User> entity = new HttpEntity<>(user, headers);

		// call site
		ResponseEntity<String> response = restTemplate.exchange(localhostLink(), HttpMethod.POST, entity, String.class);

		// test
		int expectedHeader = 409;
		assertEquals(expectedHeader, response.getStatusCodeValue());
	}

	/**
	 * Create new user with the same link as a user entity in the DB (unique links!)
	 * 
	 * Expected HTTP Header 409 (Conflict)
	 */
	@Test
	public void test05_createNewUserWithExistingLink() {
		logger.info("TEST> Create new user with the same link as a user entity in the DB!");
		// create user
		User user = new User();
		user.setForename("Max");
		user.setSurname("Mustermann");
		user.setPosition("Facility Manager");
		user.setLink("https://github.com/RobTain");

		// build web call
		HttpEntity<User> entity = new HttpEntity<>(user, headers);

		// call site
		ResponseEntity<String> response = restTemplate.exchange(localhostLink(), HttpMethod.POST, entity, String.class);

		// test
		int expectedHeader = 409;
		assertEquals(expectedHeader, response.getStatusCodeValue());
	}

	/**
	 * Create user with missing input values
	 * 
	 * Expected HTTP Header: 201 (Created) 
	 * Expected String representation:
	 * "forename":"Robert","surname":"Koenig","position":"null", "link":"null
	 */
	@Test
	public void test06_createUserWithMissingInputs() {
		logger.info("TEST> Create user with missing input values!");
		// create user
		User user = new User();
		user.setForename("Max");
		user.setSurname("Mustermann");

		// build web call
		HttpEntity<User> entity = new HttpEntity<>(user, headers);

		// call site
		ResponseEntity<String> response = restTemplate.exchange(localhostLink(), HttpMethod.POST, entity, String.class);

		// test
		int expectedHeader = 201;
		assertEquals(expectedHeader, response.getStatusCodeValue());

		// check String representation
		response = restTemplate.exchange(localhostLink(), HttpMethod.GET, entity, String.class);

		// test
		String expectedStringRepresentation = "\"forename\":\"" + user.getForename() + "\",\"surname\":\""
				+ user.getSurname() + "\",\"position\":\"" + user.getPosition() + "\",\"link\":\"" + user.getLink();
		assertThat(response.getBody()).contains(expectedStringRepresentation);
	}

	
	/**
	 * Create multiple Users. 
	 * 
	 *  Expected HTTP Header: 201 (Created) 
	 *  Expected String representation: site.contains(user)
	 */
	@Test
	public void test07_createMultipleUsers() {
		logger.info("TEST> Create multiple users!");
		// create user
		User user = new User();
		user.setForename("Simon");
		user.setSurname("Güll");
		user.setPosition("Java Developer");
		user.setLink("https://github.com/Vadammt");

		// build web call
		HttpEntity<User> entity = new HttpEntity<>(user, headers);

		// call site
		ResponseEntity<String> response = restTemplate.exchange(localhostLink(), HttpMethod.POST, entity, String.class);

		// check String representation
		response = restTemplate.exchange(localhostLink(), HttpMethod.GET, entity, String.class);

		String createdUser = response.getBody();
		
		// create second user
		user.setForename("Kevin");
		user.setSurname("Glöckl");
		user.setPosition("Quality Engineer");
		user.setLink("https://github.com/Caritrian");

		// build web call
		entity = new HttpEntity<>(user, headers);

		// call site
		response = restTemplate.exchange(localhostLink(), HttpMethod.POST, entity, String.class);

		// check String representation
		response = restTemplate.exchange(localhostLink(), HttpMethod.GET, entity, String.class);
	
		// test
		assertThat(response.getBody()).contains(createdUser.replace("]", ""));
	}

	
	/**
	 * Delete existing user (Max Mustermann with id = 2)
	 * 
	 * Expected HTTP Header: 204 (No Content) 
	 * Expected String representation: id = 2 not displayed 
	 */
	@Test
	public void test08_deleteValidUser() {
		logger.info("TEST> Delete existing user!");

		// build web call
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		// call site
		ResponseEntity<String> response = restTemplate.exchange(localhostLink() + "/2/", HttpMethod.DELETE, 
				entity, String.class);
		
		// test
		int expectedHeader = 204;
		assertEquals(expectedHeader, response.getStatusCodeValue());

		// check String representation
		response = restTemplate.exchange(localhostLink(), HttpMethod.GET, entity, String.class);
		
		// test
		String expectedStringRepresentation = "\"id\": 2";
		assertThat(!response.getBody().contains(expectedStringRepresentation));
	}
	
	
	/**
	 * Delete an deleted user (id = 2)
	 * 
	 * Expected HTTP Header: 404 (No Found) 
	 */
	@Test
	public void test09_deleteDeletedUser() {
		logger.info("TEST> Delete an deleted user!");

		// build web call
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		// call site
		ResponseEntity<String> response = restTemplate.exchange(localhostLink() + "/2/", HttpMethod.DELETE, 
				entity, String.class);
		
		// test
		int expectedHeader = 404;
		assertEquals(expectedHeader, response.getStatusCodeValue());
	}
	
	
	/**
	 * Delete a not existing user (id = 100)
	 * 
	 * Expected HTTP Header: 404 (No Found) 
	 */
	@Test
	public void test10_deleteNotValidUser() {
		logger.info("TEST> Delete a not existing user!");

		// build web call
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		// call site
		ResponseEntity<String> response = restTemplate.exchange(localhostLink() + "/100/", HttpMethod.DELETE, 
				entity, String.class);
		
		// test
		int expectedHeader = 404;
		assertEquals(expectedHeader, response.getStatusCodeValue());
	}
	
	/**
	 * Update an existing user (id = 1)
	 * 
	 * Expected HTTP Header: 404 (No Found)
	 * Expected String representation: "forename":"update"
	 */
	@Test
	public void test11_updateValidUser() {
		logger.info("TEST> Update an existing user!");
	
		// update user values
		String updateVariable = "update";
		
		User user = new User();
		user.setForename(updateVariable);
		user.setLink(updateVariable);
		
		// set valid MediaType
		headers.setContentType(MediaType.APPLICATION_JSON);
	
		// build web call
		HttpEntity<User> entity = new HttpEntity<>(user, headers);

		// call site
		ResponseEntity<String> response = restTemplate.exchange(localhostLink() + "/1/", 
				HttpMethod.PUT, entity, String.class);
		
		// test
		int expectedHeader = 200;
		assertEquals(expectedHeader, response.getStatusCodeValue());
		
		// check string representation
		response = restTemplate.exchange(localhostLink() + "/1/", HttpMethod.GET, 
					entity, String.class);
		
		// test
		String expectedStringRepresentation = "\"forename\":\"" + updateVariable;
		assertThat(response.getBody()).contains(expectedStringRepresentation);		
	}
	
	
	/**
	 * Update an existing user with same values (id = 3)
	 * 
	 * Expected HTTP Header: 409 (Conflict)
	 */
	@Test
	public void test12_updateDublicate() {
		logger.info("TEST> Update an user with same values!");
	
		// set user
		User user = new User();
		user.setForename("Simon");
		user.setSurname("Güll");
		user.setPosition("Java Developer");
		user.setLink("https://github.com/Vadammt");
		
		// set valid MediaType
		headers.setContentType(MediaType.APPLICATION_JSON);
	
		// build web call
		HttpEntity<User> entity = new HttpEntity<>(user, headers);

		// call site
		ResponseEntity<String> response = restTemplate.exchange(localhostLink() + "/1/", 
				HttpMethod.PUT, entity, String.class);
		
		// test
		int expectedHeader = 409;
		assertEquals(expectedHeader, response.getStatusCodeValue());
	}
	
	/**
	 * Update a deleted user (id = 2)
	 * 
	 * Expected HTTP Header: 404 (Not Found)
	 */
	@Test
	public void test12_updateDeletedUser() {
		logger.info("TEST> Update a deleted user!");
	
		// set user
		User user = new User();
		user.setForename("test");

		// set valid MediaType
		headers.setContentType(MediaType.APPLICATION_JSON);
	
		// build web call
		HttpEntity<User> entity = new HttpEntity<>(user, headers);

		// call site
		ResponseEntity<String> response = restTemplate.exchange(localhostLink() + "/2/", 
				HttpMethod.PUT, entity, String.class);
		
		// test
		int expectedHeader = 404;
		assertEquals(expectedHeader, response.getStatusCodeValue());
	}
	
	
	/**
	 * Update a not existing user (id = 100)
	 * 
	 * Expected HTTP Header: 404 (Not Found)
	 */
	@Test
	public void test12_updateNonExistingUser() {
		logger.info("TEST> Update a not existing user!");
	
		// set user
		User user = new User();
		user.setForename("test");

		// set valid MediaType
		headers.setContentType(MediaType.APPLICATION_JSON);
	
		// build web call
		HttpEntity<User> entity = new HttpEntity<>(user, headers);

		// call site
		ResponseEntity<String> response = restTemplate.exchange(localhostLink() + "/100/", 
				HttpMethod.PUT, entity, String.class);
		
		// test
		int expectedHeader = 404;
		assertEquals(expectedHeader, response.getStatusCodeValue());
	}
	
	
	private String localhostLink() {
		return "http://localhost:" + port + "/users/";
	}

}
