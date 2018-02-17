package me.remind.userMicroservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import me.remind.userMicroservice.model.User;
import me.remind.userMicroservice.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RESTful web service controller for Request/Response communication with clients.
 * 
 * @author RobSoft
 */
@RestController
public class UserController {

	@Autowired
	private UserService userService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * List all user entities
	 * 
	 * @return all users
	 */
	@RequestMapping(value = { "/users", "/users/" }, method = RequestMethod.GET)
	public ResponseEntity<List<User>> findAllUser() {
		// fetch all users and return list
		List<User> users = userService.findAllUser();
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	/**
	 * Searches for the user entity with @param id
	 * 
	 * @param id the id to be searched for
	 * @return user with the id or an HTTP 404
	 */
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> findUserById(@PathVariable("id") Long id) {
		
		// fetch certain user
		User user = userService.findUserById(id);

		// check not existing user
		if (user == null) {
			logger.error("User with id {} not found.", id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			// return user
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}		
	}

	/**
	 * Delete requested user entity with @param id
	 * 
	 * @param id the id to be searched for
	 * @return HTTP 204 (user deleted) or HTTP 404 (user not found)
	 */
	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
		logger.info("Fetching & Deleting User with id {}", id);

		// fetch certain user
		User user = userService.findUserById(id);

		// check not existing user
		if (user == null) {
			logger.error("Unable to delete user with id {}.", id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			
			// delete user
			logger.info("Delete user");
			userService.deleteUserById(id);
			return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Delete all user entities from DB
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
	 * Create a new user entity
	 * 
	 * @param user the user to be added into DB
	 * @return HTTP 409 (user exist) or HTTP 201 (user created)
	 */
	@RequestMapping(value = "/users/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createUser(@RequestBody User user) {
		logger.info("Creating User : {}", user);
		
		// check valid input
		if (userService.userExist(user)) {
			logger.error("Unable to create user. User already exist!");
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} else {
			
			// create user
			logger.info("Create user");
			userService.createUser(user);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
	}

	/**
	 * Edit a user entity
	 * 
	 * @param id the id to be searched for
	 * @param user the user to be modified and written into DB
	 * @return HTTP 200 (user updated) or HTTP 404 (User not found)
	 */
	@RequestMapping(value = "/users/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user) {
		logger.info("Updating User with id {}", id);
		
		// find user
		User currentUser = userService.findUserById(id);
		
		// check not existing user
		if (currentUser == null) {
			logger.error("Unable to update user with id {}.", id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			// update input
			if (!(user.getForename() == null)) {
				currentUser.setForename(user.getForename());
			}
			if (!(user.getSurname() == null)) {
				currentUser.setSurname(user.getSurname());
			}
			if (!(user.getPosition() == null)) {
				currentUser.setPosition(user.getPosition());
			}
			if (!(user.getLink() == null)) {
				currentUser.setLink(user.getLink());
			}
		}

		// check valid input
		if (userService.userExist(currentUser)) {
			logger.error("Unable to update user. User already exist!");
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} else {
			// update user into DB
			logger.info("Update user");
			userService.updateUser(currentUser);
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	/**
	 * List Github-Repositories and coding language from a certain user:
	 * 
	 * @param id the id to be searched for
	 * @return external informations from user id or an HTTP 404
	 */
	@RequestMapping(value = "/users/{id}/repositories", method = RequestMethod.GET)
	public ResponseEntity<String> findRepositoriesFromUserId(@PathVariable("id") Long id) {
				
		// fetch certain user
		User user = userService.findUserById(id);

		// check not existing user
		if (user == null) {
			logger.error("User with id {} not found", id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			// find external input
			String list = userService.findExternalResources(user.getLink());
			
			// check for a filled list (prevent HTTP 404)
			if (!list.equals("")) {
			return new ResponseEntity<String>(list, HttpStatus.OK);
			} else {
				logger.error("HTTP 404: Check Link!");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}
	}
}