package com.shopme.customer;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.TypeRegister;
import com.shopme.secutity.CustomerUserDetails;
import com.shopme.secutity.oauth.CustomerOAuth2User;
import com.shopme.setting.EmailSetting;
import com.shopme.setting.SettingService;
import com.shopme.setting.Utility;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private SettingService settingService;
	
	@GetMapping("/register")
	public String viewRegister(Model model) {
		model.addAttribute("listCountry", customerService.listCountry());
		model.addAttribute("listState", customerService.listState());
		model.addAttribute("customer",new Customer());
		return "/customers/register";
	}
	
	@PostMapping("/customers/register")
	public String addCustomer(Customer customer, Model model,
			HttpServletRequest request) 
					throws UnsupportedEncodingException, MessagingException {
		if (!customerService.isUniqueEmail(customer.getEmail(), customer.getId())) {
			model.addAttribute("listCountry", customerService.listCountry());
			model.addAttribute("listState", customerService.listState());
			model.addAttribute("unique_email", "Email is exist");
			return "/customers/register";
		}
		
		customer.setAutheticationType(TypeRegister.DATABASE);
		
		customerService.registerCustomer(customer);
		customerService.save(customer);
		sendCustomerVerify(request, customer);

		model.addAttribute("pageTitile", "Registration Succeded");
		
		return "/customers/register_success";
	}

	private void sendCustomerVerify(HttpServletRequest request, Customer customer) 
			throws UnsupportedEncodingException, MessagingException {
		EmailSetting emailSetting = new EmailSetting(settingService.getEmail());
		
		
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSetting);
		mailSender.setDefaultEncoding("utf-8");

		String toAddress = customer.getEmail();
		String subject = emailSetting.getCustomerVerifySubject();
		String content = emailSetting.getCustomerVerifyContent();
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		
		helper.setFrom(emailSetting.getFromAddress(), emailSetting.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		content = content.replace("[[name]]", customer.getFullName());
		
		String urlVerify = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
		content = content.replace("[[URL]]", urlVerify);
		
		helper.setText(content, true);
		
		mailSender.send(message);
	}
	
	@GetMapping("/verify")
	public String verifyAccount(@Param("code")String code, Model model) {
		boolean verify = customerService.verify(code);
		return "/customers/register/" + (verify ? "verify_success" : "verify_fail");
	}
	
	@GetMapping("/customers/managementAccount")
	public String viewManagementAccount(Model model) {
		return "/customers/management_account/editProfile";
	}
	
	@GetMapping("/customers/editProfile")
	public String viewEditProfile(Model model, HttpServletRequest httpServletRequest) {
//		Object principal = httpServletRequest.getUserPrincipal();
//		String principalType = principal.getClass().getName();
//		
//		System.out.println("Principal Name: " + httpServletRequest.getUserPrincipal().getName());
//		System.out.println(principalType);
		//UsernamePasswordAuthenticationToken
		//OAuth2AuthenticationToken
		//RememberMeAuthenticationToken
		
		model.addAttribute("listCountry", customerService.listCountry());
		model.addAttribute("listState", customerService.listState());
		
		String email = getEmailOfAuthenticatedCustomer(httpServletRequest);
		Customer customer = customerService.getByEmail(email);
		
		model.addAttribute("customer", customer);
		return "/customers/management_account/editInfoAccount";
	}
	
	private String getEmailOfAuthenticatedCustomer(HttpServletRequest httpServletRequest) {
		String customerEmail = null;
		Object principal = httpServletRequest.getUserPrincipal();
		if (principal instanceof UsernamePasswordAuthenticationToken || 
				principal instanceof RememberMeAuthenticationToken) {
			customerEmail = httpServletRequest.getUserPrincipal().getName();
		} else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken auth2Token = (OAuth2AuthenticationToken)principal;
			CustomerOAuth2User auth2User = (CustomerOAuth2User)auth2Token.getPrincipal();
			customerEmail = auth2User.getEmail();
		}
		
		return customerEmail;
	}
	
	@PostMapping("/update_account_details")
	public String updateAccountDetail(Customer customer, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		customerService.updateCustomer(customer);
		
		String redirect = request.getParameter("redirect");
		if (redirect.equals("cart")) {
			redirectAttributes.addFlashAttribute("message", "Cập nhập địa chỉ thành công.");
			return "redirect:/" + redirect;
		}

		redirectAttributes.addFlashAttribute("message", "Your account details have been updated.");
		
		return "redirect:/customers/editProfile";
	}
	
	@GetMapping("/forgot_password")
	public String forgotPassword(Model model) {
		return "/customers/forgot_password/forgot_password";
	}
	
	@PostMapping("/confirm_mail_forgot")
	public String confirmMailForgot(Model model, HttpServletRequest request) {
		String email = request.getParameter("email");
		try {
			String token = customerService.updateVerifyTokenForgotPassword(email);
			String link = Utility.getSiteURL(request) + "/forgot_password_token?token=" + token;
			sendMail(link, email);
			return "/customers/forgot_password/confirm_email_success";
		} catch (CustomerNotFoundException e) {
			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "Could not send mail");
		}
		return "/customers/forgot_password/forgot_password";
	}
	
	private void sendMail(String link, String email) throws UnsupportedEncodingException, MessagingException {
		EmailSetting emailSetting = new EmailSetting(settingService.getEmail());
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSetting);

		String toAddress = email;
		String subject = "Reset password";
		String content = "<p>Hello, </p>"
				+ "<p>Click link reset password: <a href=\""+link+"\">Click me</a></p>";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		

		helper.setFrom(emailSetting.getFromAddress(), emailSetting.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		helper.setText(content, true);
		
		mailSender.send(message);
	}
	
	@GetMapping("/forgot_password_token")
	public String viewResetpassword(Model model, @Param("token")String token) {
		Customer customer = customerService.getCustomerByToken(token);
		if (customer != null) {
			model.addAttribute("token", token);
			return "/customers/forgot_password/reset_password";
		} else {
			return "/customers/forgot_password/verify_fail";
		}
	}
	
	@PostMapping("/reset_password")
	public String resetPassword(HttpServletRequest request) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		
		customerService.resetPassword(token, password);
		
		return "/customers/forgot_password/verify_success";
	}
	
	
}
