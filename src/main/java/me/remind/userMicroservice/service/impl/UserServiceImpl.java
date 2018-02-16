package me.remind.userMicroservice.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import me.remind.userMicroservice.model.GitHub;
import me.remind.userMicroservice.model.User;
import me.remind.userMicroservice.service.UserService;
import static java.util.Arrays.asList;



@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
		

	/**
	 * List all user entities.
	 */
	@Override
	public List<User> findAllUser() {
		final String sql = "SELECT * FROM USER";
		List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class));
		return users;
	}

	/**
	 * Find user entity by ID.
	 */
	@Override
	public User findUserById(Long id) {
		final String sql = "SELECT * FROM USER WHERE ID=?";
		User user = new User();

		try {
			// try to find id, catch empty result if id is not valid
			Object queryForObject = jdbcTemplate.queryForObject(sql, new Object[] { id },
					new BeanPropertyRowMapper<User>(User.class));
			user = (User) queryForObject;
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			return null;
		}
		return user;
	}

	/**
	 * Delete user entity by ID.
	 */
	@Override
	public void deleteUserById(Long id) {
		final String sql = "DELETE FROM USER WHERE ID=?";
		jdbcTemplate.update(sql, new Object[] { id });
	}

	/**
	 * Delete all user entities.
	 */
	@Override
	public void deleteAllUsers() {
		final String sql = "DELETE FROM USER";
		jdbcTemplate.update(sql);
	}

	/**
	 * Check existence of a user
	 */
	@Override
	public boolean userExist(User user) {
		boolean userExist = false;
		List<User> users = findAllUser();

		// search through list and break look if user exist
		if (!users.isEmpty()) {
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).equals(user)) {
					userExist = true;
					break;
				}
			}
		}
		return userExist;
	}

	/**
	 * Create user entity
	 */
	@Override
	public void createUser(User user) {
		final String sql = "INSERT INTO USER(FORENAME, SURNAME, POSITION, LINK) VALUES ('" + user.getForename() + "', '"
				+ user.getSurname() + "', '" + user.getPosition() + "', '" + user.getLink() + "');";
		jdbcTemplate.update(sql);
	}

	/**
	 * Update user entity
	 */
	@Override
	public void updateUser(User user) {
		final String sql = "UPDATE USER SET FORENAME = ?,  SURNAME = ?, POSITION = ?, LINK = ? WHERE ID = ?";
		jdbcTemplate.update(sql, new Object[] { user.getForename(), user.getSurname(), user.getPosition(), 
				user.getLink(), user.getId() });
	}

	/**
	 * import API resources from external sites
	 */
	@Override
	public String findExternalResources(String link) {
		String returnValue = "", url = "";
	
		final RestTemplate restTemplate = new RestTemplate();
		// check external links
		if (link.contains("https://github.com/")) {
			// build url
			url += "https://api.github.com/users/" + link.toLowerCase().replace("https://github.com/", "") + "/repos";
			
			// add external resources
			List<GitHub> list;
			try {
			 list = asList(restTemplate.getForObject(url, GitHub[].class)); 
			// catch HTTP 404 
			} catch(org.springframework.web.client.HttpClientErrorException e) {
				
				return returnValue;
			}			
			for (GitHub gitHub : list) {
				returnValue += gitHub.toString();
			}
			
		}
		//TODO add more external resources	
		
		return returnValue;

	}
}
