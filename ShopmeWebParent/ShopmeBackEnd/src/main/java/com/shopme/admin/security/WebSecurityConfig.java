package com.shopme.admin.security;

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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
	@Bean
    public UserDetailsService userDetailsService() {
        return new ShopmeUserDetailService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
        	.requestMatchers("/users/**").hasAuthority("Admin")
        	.requestMatchers("/categories/**").hasAnyAuthority("Admin", "Editor")
        	.requestMatchers("/branchs/**").hasAnyAuthority("Admin", "Editor")
        	.requestMatchers("/products/**").hasAnyAuthority("Admin", "Editor", "Shipper", "Salesperson")
        	.requestMatchers("/customers/**").hasAnyAuthority("Admin", "Salesperson")
        	.requestMatchers("/shipping/**").hasAnyAuthority("Admin", "Salesperson")
        	.requestMatchers("/oders/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")
        	.requestMatchers("/report/**").hasAnyAuthority("Admin", "Salesperson")
        	.requestMatchers("/articles/**").hasAnyAuthority("Admin", "Editor")
        	.requestMatchers("/setting/**").hasAnyAuthority("Admin")
        	.anyRequest().authenticated()
	            .and()
	            .formLogin()
	            	.loginPage("/login")
	            	.usernameParameter("email")
	            	.permitAll()
	            .and().logout().permitAll()
	            .and().rememberMe()
			            .userDetailsService(userDetailsService())
		                .key("uniqueAndSecret")
		                .tokenValiditySeconds(86400)
	            .and().authenticationProvider(authenticationProvider())
            ;
        
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

