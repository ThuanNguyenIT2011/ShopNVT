package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoeyTest {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userThuan = new User("thuanit2011@gmail.com", "Nguyen", "Thuan", "$2a$12$ZtZZWPzE8kSThg6Y2ux12ePSmISxdpvZcLwl0rvoTR/XBmltwIAjO");
		userThuan.addRole(roleAdmin);
		
		userRepository.save(userThuan);
	}
	
	@Test
	public void testCreateUserWithTwoRole() {
		User userGavi = new User("gavi@gmail.com", "Gavi2023", "Gavi", "20112002");
		
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userGavi.addRole(roleEditor);
		userGavi.addRole(roleAssistant);
		
		User saveUser = userRepository.save(userGavi);
		
		assertThat(saveUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListUser() {
		Iterable<User> listUsers = (List<User>)userRepository.findAll();
		listUsers.forEach(
					(user) -> System.out.println(user)
				);
	}
	
	@Test
	public void testGetUserById() {
		User user = userRepository.findById(1).get();
		
		System.out.println(user.getLastName());
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testUpdateUserById() {
		User user = userRepository.findById(1).get();
		user.setEnabled(true);
		
		userRepository.save(user);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User user = userRepository.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSaleperson = new Role(2);
		
		user.getRoles().remove(roleEditor);
		user.addRole(roleSaleperson);
		
		userRepository.save(user);
	}
	
	@Test
	public void testDeleteUserById() {
		Integer userId = 2;
		userRepository.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "thuanit2011@gmail.com";
		User user = userRepository.getUserByEmail(email);
		System.out.println(user);
//		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountUserById() {
		int count = userRepository.countById(0).intValue();
		System.out.println(count);
		assertThat(count).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateDisabledUser() {
		Integer id = 24;
		userRepository.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testEnabledUser() {
		Integer id = 24;
		userRepository.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;
		PageRequest pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userRepository.findAll(pageable);
		List<User> listUser = page.getContent();
		
		listUser.forEach(user -> System.out.println(user));
		assertThat(listUser.size()).isEqualTo(pageSize);
	}
	
	@Test
	public void testFilterUser() {
		int pageNumber = 0;
		int pageSize = 4;
		PageRequest pageRequest= PageRequest.of(pageNumber, pageSize);
		Page<User> listUserFilter = userRepository.findAll("bruce", pageRequest);
		listUserFilter.forEach(user -> System.out.println(user));
	}
}
