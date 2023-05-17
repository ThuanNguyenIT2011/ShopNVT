package com.shopme;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTest {
	@Autowired 
	private CustomerRepository repo;
	
	@Autowired 
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateCustomer1() {
		Integer countryId = 1; // USA
		Country country = entityManager.find(Country.class, countryId);

		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Thuan");
		customer.setLastName("Nguyen");
		customer.setPassword("password123");
		customer.setEmail("nguyenvanthuan20112002@gmail.com");
		customer.setPhoneNumber("0934932775");
		customer.setAddressLine1("bac ruong tanh linh");
		customer.setCity("Binh Thuan");
		customer.setState("Binh Thuan");
		customer.setPostalCode("95867");
		customer.setCreatedTime(new Date());

		Customer savedCustomer = repo.save(customer);

		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateCustomer2() {
		Integer countryId = 106; // India
		Country country = entityManager.find(Country.class, countryId);

		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Thanh");
		customer.setLastName("Le");
		customer.setPassword("password456");
		customer.setEmail("thuannit2011@gmail.com");
		customer.setPhoneNumber("113");
		customer.setAddressLine1("mang to tanh linh");
		customer.setAddressLine2("thu duc quan 9");
		customer.setCity("TP HCM");
		customer.setState("Binh Thuan");
		customer.setPostalCode("400013");
		customer.setCreatedTime(new Date());

		Customer savedCustomer = repo.save(customer);

		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListCustomers() {
		Iterable<Customer> customers = repo.findAll();
		customers.forEach(System.out::println);

		assertThat(customers).hasSizeGreaterThan(1);
	}
	
	
	
}
