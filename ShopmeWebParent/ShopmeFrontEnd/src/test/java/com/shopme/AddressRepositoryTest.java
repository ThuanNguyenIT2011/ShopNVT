package com.shopme;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.address.AddressRepository;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AddressRepositoryTest {
	@Autowired 
	private AddressRepository repo;
	
	@Test
	public void testAddNew() {
		Integer customerId = 29;
		Integer countryId = 1; // VN

		Address newAddress = new Address();
		newAddress.setCustomer(new Customer(customerId));
		//newAddress.setCountry(new Country(countryId));
		newAddress.setFirstName("Thuận");
		newAddress.setLastName("Nguyễn");
		newAddress.setPhoneNumber("0943932775");
		newAddress.setAddressLine1("Tánh Linh");
		newAddress.setCity("Phan thiết");
		newAddress.setState(new State(3));
		newAddress.setPostalCode("2011");

		Address savedAddress = repo.save(newAddress);

		assertThat(savedAddress).isNotNull();
		assertThat(savedAddress.getId()).isGreaterThan(0);
	}
}
