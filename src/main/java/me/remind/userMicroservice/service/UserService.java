package me.remind.userMicroservice.service;

import org.springframework.data.repository.CrudRepository;
import me.remind.userMicroservice.model.User;


public interface UserService extends CrudRepository<User, Long>{
	
}
