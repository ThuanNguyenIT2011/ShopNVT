package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {
	public static final int USERS_PER_PAGE = 4;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> listAll(){
		return (List<User>) userRepo.findAll(Sort.by("firstName").ascending());
	}
	
	public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
 		PageRequest pageRequest = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);
 		
 		if (keyword != null) {
 			return userRepo.findAll(keyword, pageRequest);
 		}
 		
		return userRepo.findAll(pageRequest);
	}
	
	public List<Role> listRole() {
		return (List<Role>) roleRepo.findAll();
	}
	
	public User save(User user) {
		boolean isUpdatingUser = (user.getId() != null);
		if(isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get();
			if(user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);			
		}
		return userRepo.save(user);
	}
	
	public User updateUser (User userInForm) {
		User userInDb = userRepo.findById(userInForm.getId()).get();
		
		if (!userInForm.getPassword().isEmpty()) {
			userInDb.setPassword(userInForm.getPassword());
			encodePassword(userInDb);
		}
		
		if (userInForm.getPhotos() != null) {
			userInDb.setPhotos(userInForm.getPhotos());
		}
		
		userInDb.setFirstName(userInForm.getFirstName());
		userInDb.setLastName(userInForm.getLastName());
		
		return userRepo.save(userInDb);
	}
	
	private void encodePassword(User user) {
		String passEncode = passwordEncoder.encode(user.getPassword());		
		user.setPassword(passEncode);
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		User user = userRepo.getUserByEmail(email);
		if (user == null) {
			return true;
		}
		
		boolean isCreatingNew = (id == null);
		
		if(isCreatingNew) {
			if(user != null) {
				return false;
			}
		} else {
			if(user.getId() != id) {
				return false;
			}
		}
		return true;
	}
	
	public User get(String email) {
		return userRepo.getUserByEmail(email);
	}

	public User get(Integer id) throws UserNotException {
		try {
			return userRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new UserNotException("Không tim thấy nhân viên ID " + id);
		}
	}
	
	public void delete(Integer id) throws UserNotException {
		Long countUser = userRepo.countById(id);
		if (countUser == 0 || countUser == null) {
			throw new UserNotException("Không tim thấy nhân viên ID " + id);
		} else {
			userRepo.deleteById(id);
		}
	}
	
	public void updateUserEnabled(Integer id, boolean enabled) throws UserNotException  {
		Long countUser = userRepo.countById(id);
		if (countUser == 0 || countUser == null) {
			throw new UserNotException("Không tim thấy nhân viên ID " + id);
		} else {
			userRepo.updateEnabledStatus(id, enabled);
		}
	}
}
