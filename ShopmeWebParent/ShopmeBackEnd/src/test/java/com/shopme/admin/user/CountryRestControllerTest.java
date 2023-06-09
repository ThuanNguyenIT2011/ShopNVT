package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.entity.Country;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTest {
	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;
	
	@Test
	@WithMockUser(username = "thuan@codejava.net", password = "123456", roles = "ADMIN")
	public void testContries() throws Exception {
		String url = "/countries/list";
//		mockMvc.perform(get(url))
//			.andExpect(status().isOk())
//			.andDo(print());
		MvcResult result = mockMvc.perform(get(url))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
		
		String jsonRespone = result.getResponse().getContentAsString();
		System.out.println(jsonRespone);
		
		Country[] countries = objectMapper.readValue(jsonRespone, Country[].class);
		for (Country country : countries) {
			System.out.println(country);
		}
		
		assertThat(countries).hasSizeGreaterThan(0);
	}
	
	@Test
	@WithMockUser(username = "thuan@codejava.net", password = "123456", roles = "ADMIN")
	public void testCreateCountry() throws JsonProcessingException, Exception {
		String url = "/countries/save";
		Country country = new Country("Canada", "CA");
		MvcResult result = mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(country))
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();
		
		String response = result.getResponse().getContentAsString();
		System.out.println("Coutry Id: " + response);
	}
}
