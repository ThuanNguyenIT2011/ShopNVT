package com.shopme.secutity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.shopme.secutity.oauth.CustomerOAuth2User;
import com.shopme.secutity.oauth.CustomerOAuth2UserService;
import com.shopme.secutity.oauth.OAuth2LoginSuccessHandler;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
	@Autowired private CustomerOAuth2UserService customerOAuth2UserService;
	@Autowired private OAuth2LoginSuccessHandler auth2LoginSuccessHandler;
	@Autowired private DatabaseLoginSuccessSuccessHandler databaseLoginSuccessSuccessHandler;
	
	@Bean(name = "passwordEncoder")
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	} 
	
	@Bean
    public UserDetailsService userDetailsService() {
        return new CustomerUserDetailsService();
    }
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
				.requestMatchers("/customers", "/cart").authenticated()
				.anyRequest().permitAll()
				.and()
				.formLogin()
					.loginPage("/login")
					.usernameParameter("email")
					.successHandler(databaseLoginSuccessSuccessHandler)
					.permitAll()
				.and()
				.oauth2Login()
					.loginPage("/login")
					.userInfoEndpoint()
					.userService(customerOAuth2UserService)
					.and()
					.successHandler(auth2LoginSuccessHandler)
				.and()
				.logout().logoutUrl("/doLogout").permitAll()
				.and()
				.rememberMe()
	            	.userDetailsService(userDetailsService())
	            	.key("uniqueAndSecret")
	            	.tokenValiditySeconds(86400)
				.and()
				.authenticationProvider(authenticationProvider());
		
		return http.build();
		
	}
	
	@Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
	
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**", "/style/**", "/fontawesome/**");
    }

}

