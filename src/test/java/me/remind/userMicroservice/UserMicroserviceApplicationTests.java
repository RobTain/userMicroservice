package me.remind.userMicroservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import me.remind.userMicroservice.controller.UserController;
import me.remind.userMicroservice.model.User;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserMicroserviceApplicationTests {

	@MockBean
	private UserController userController;
	
	
	@Test
	public void createValidUser() {
		User user = new User();
		user.setForename("Robert");
		user.setSurname("Koenig");
		user.setPosition("Java Developer");
		user.setLink("https://github.com/RobTain");
		
		userController.createUser(user);		
		
	 }



}
