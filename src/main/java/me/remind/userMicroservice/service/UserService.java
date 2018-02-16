package me.remind.userMicroservice.service;

import java.util.List;
import org.springframework.stereotype.Repository;
import me.remind.userMicroservice.model.User;

@Repository
public interface UserService  {

	public List<User> findAllUser();

	public User findUserById(Long id);

	public void deleteUserById(Long id);

	public void deleteAllUsers();

	public boolean userExist(User user);

	public void createUser(User user);

	public void updateUser(User user);

	public String findExternalResources(String link);

}
