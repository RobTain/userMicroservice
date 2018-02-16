package me.remind.userMicroservice.service;

import java.util.List;


import org.springframework.stereotype.Repository;

import me.remind.userMicroservice.model.User;

@Repository
public interface UserService  {

	public List<User> findAll();

	public User findById(Long id);

	public void deleteUserById(Long id);

	public void deleteAllUsers();

	public boolean userExist(User user);

	public void createUser(User user);

}
