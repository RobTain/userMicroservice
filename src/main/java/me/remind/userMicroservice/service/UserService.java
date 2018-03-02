package me.remind.userMicroservice.service;

import org.springframework.data.repository.CrudRepository;
import me.remind.userMicroservice.model.User;


public interface UserService extends CrudRepository<User, Long>{
	
	
	
	
	

//	public List<User> findAllUser();
//
//	public User findUserById(Long id);
//
//	public void deleteUserById(Long id);
//
//	public void deleteAllUsers();
//
//	public boolean userExist(User user);
//
//	public void createUser(User user);
//
//	public void updateUser(User user);
//
//	public String findExternalResources(String link);

}
