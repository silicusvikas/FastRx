package com.parkwoodrx.fastrx.webservice.test;

/*import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import com.parkwoodrx.fastrx.FastrxApplication;
import com.parkwoodrx.fastrx.model.User;
import com.parkwoodrx.fastrx.service.UserService;
import com.parkwoodrx.fastrx.webservice.UserController;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = FastrxApplication.class)
@WebAppConfiguration*/
public class UserControllerTest {

	/*@InjectMocks
	private UserController controllerUT = new UserController();

	@Mock
	private UserService service;

	@Mock
	private User user;
	
	@Mock
	private User user2;

	@Test
	public void testMockCreation() {
		assertNotNull(user);
	}

	@Test
	public void addUser(){
		long id = 1;
		when(service.addUser(user, "Pintoo",1,1)).thenReturn(id);
		assertNotNull(id);
		assertEquals(id, service.addUser(user, "Pintoo",1,1));
	}
	
	@Test
	public void getUserList() {
		when(service.getUserList()).thenReturn(Arrays.asList(user, user, user));
		assertEquals(3, service.getUserList().size());
	}
	
	@Test
	public void getUserByIdAndPassword() {
		when(service.getUserByIdAndPassword(1,"Password")).thenReturn(user);
		assertEquals(user, service.getUserByIdAndPassword(1,"Password"));
		assertNotEquals(user2, service.getUserByIdAndPassword(1,"Password"));
	}*/

}
