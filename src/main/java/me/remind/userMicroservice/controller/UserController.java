package me.remind.userMicroservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import me.remind.userMicroservice.model.User;
import me.remind.userMicroservice.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * List all users from DB
	 * 
	 * @return all users
	 */
	@RequestMapping(value = { "/users", "/users/" }, method = RequestMethod.GET)
	public ResponseEntity<List<User>> findAll() {
		List<User> users = userService.findAll();
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	/**
	 * Searches user DB for the user with @param id
	 * 
	 * @param id
	 *            the id to be searched for
	 * @return user with the id or an HTTP 404
	 */
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> findById(@PathVariable("id") Long id) {
		User user = userService.findById(id);
		if (user == null) {
			logger.error("User with id {} not found", id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
	}

	/**
	 * Delete requested user with @param id
	 * 
	 * @param id
	 *            the id to be searched for
	 * @return HTTP 204 (user deleted) or HTTP 404 (user not found)
	 */
	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
		logger.info("Fetching & Deleting User with id {}", id);

		User user = userService.findById(id);
		if (user == null) {
			logger.error("Unable to delete user with id {} -> user not found", id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			userService.deleteUserById(id);
			return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Delete all users from db
	 * 
	 * @return HTTP 204 (all users deleted)
	 */
	@RequestMapping(value = { "/users", "/users/" }, method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteAllUsers() {
		logger.info("Deleting All Users");
		userService.deleteAllUsers();
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

	/**
	 * $ curl -H "Content-Type: application/json" -X POST -d '{"forename": "Max",
    "surname": "Mustermann",
    "position": "Janitor",
    "link": "https://github.com/RobTain"}' localhost:8080/users/

	 */
	@RequestMapping(value = "/users/", method = RequestMethod.POST, consumes =  MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
		logger.info("Creating User : {}", user);
		if (userService.userExist(user)) {
			logger.error("Unable to create user. User already exist!");
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} else {
			userService.createUser(user);
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
			return new ResponseEntity<String>(headers, HttpStatus.CREATED);
		}
	}

}