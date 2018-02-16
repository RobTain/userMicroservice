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

	@Override
	public List<User> findAll() {
		String sql = "SELECT * FROM USER";
		List<User> users = template.query(sql, new BeanPropertyRowMapper<User>(User.class));
		return users;
	}

	@Override
	public User findById(Long id) {
		String sql = "SELECT * FROM USER WHERE ID=?";
		User user = new User();

		try {
			Object queryForObject = template.queryForObject(sql, new Object[] { id },
					new BeanPropertyRowMapper<User>(User.class));
			user = (User) queryForObject;
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			return null;
		}
		return user;
	}

	@Override
	public void deleteUserById(Long id) {
		String sql = "DELETE FROM USER WHERE ID=?";
		template.update(sql, new Object[] { id });
	}

	@Override
	public void deleteAllUsers() {
		String sql = "DELETE FROM USER";
		template.update(sql);
	}

	@Override
	public boolean userExist(User user) {
		boolean userExist = false;
		List<User> users = findAll();
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

	@Override
	public void createUser(User user) {
		String sql = "INSERT INTO User(FORENAME, SURNAME, POSITION, LINK) values ('" + user.getForename() + 
				"', '" + user.getSurname() + "', '" + user.getPosition() + "', '" + user.getLink() +"');";
		template.update(sql);
	}

}
