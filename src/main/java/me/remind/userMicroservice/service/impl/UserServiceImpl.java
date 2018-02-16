package me.remind.userMicroservice.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import me.remind.userMicroservice.model.User;
import me.remind.userMicroservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private JdbcTemplate template;

	/**
	 * List all user entities.
	 */
	@Override
	public List<User> findAllUser() {
		String sql = "SELECT * FROM USER";
		List<User> users = template.query(sql, new BeanPropertyRowMapper<User>(User.class));
		return users;
	}

	/**
	 * Find user entity by ID. 
	 */
	@Override
	public User findUserById(Long id) {
		String sql = "SELECT * FROM USER WHERE ID=?";
		User user = new User();

		try {
			//try to find id, catch empty result if id is not valid
			Object queryForObject = template.queryForObject(sql, new Object[] { id },
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
		String sql = "DELETE FROM USER WHERE ID=?";
		template.update(sql, new Object[] { id });
	}

	/**
	 * Delete all user entities.
	 */
	@Override
	public void deleteAllUsers() {
		String sql = "DELETE FROM USER";
		template.update(sql);
	}

	/**
	 * Check existence of a user
	 */
	@Override
	public boolean userExist(User user) {
		boolean userExist = false;
		List<User> users = findAllUser();
		
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
		String sql = "INSERT INTO USER(FORENAME, SURNAME, POSITION, LINK) VALUES ('" + user.getForename() + 
				"', '" + user.getSurname() + "', '" + user.getPosition() + "', '" + user.getLink() +"');";
		template.update(sql);
	}

	/**
	 * Update user entity
	 */
	@Override
	public void updateUser(User user) {
		String sql = "UPDATE USER SET FORENAME = ?,  SURNAME = ?, POSITION = ?, LINK = ? WHERE ID = ?";
		template.update(sql,new Object[] 
				{user.getForename(), user.getSurname(), user.getPosition(), user.getLink(), user.getId()});	
	}
}
