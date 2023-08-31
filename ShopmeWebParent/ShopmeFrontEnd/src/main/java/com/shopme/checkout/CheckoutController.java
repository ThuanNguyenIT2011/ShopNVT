package com.shopme.checkout;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.address.AddressService;
import com.shopme.cart.CartItemService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.OrderTrack;
import com.shopme.common.entity.PaymentMethod;
import com.shopme.common.entity.ShippingRate;
import com.shopme.customer.CustomerService;
import com.shopme.order.OrderService;
import com.shopme.ordertrack.OrderTrackService;
import com.shopme.secutity.oauth.CustomerOAuth2User;
import com.shopme.setting.EmailSetting;
import com.shopme.setting.SettingService;
import com.shopme.setting.Utility;
import com.shopme.shippingrate.ShippingRateService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CheckoutController {
	@Autowired private CheckoutService checkoutService;
	@Autowired private ShippingRateService shippingRateService;
	@Autowired private CartItemService cartItemService;
	@Autowired private AddressService addressService;
	@Autowired private CustomerService customerService;
	@Autowired private OrderService orderService;
	@Autowired private SettingService settingService;
	@Autowired private OrderTrackService orderTrackService;
	
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
	
	@GetMapping("/checkout")
	public String viewCheckout(Model model, HttpServletRequest request) {
		String mail = getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getByEmail(mail);
		
		List<CartItem> listCartItems = cartItemService.listCartByCustomer(customer);
		
		ShippingRate shippingRate = null;
		Address address = addressService.getDefaultAddressByCustomer(customer);
	
		if (address == null) {
			shippingRate = shippingRateService.findShippingRateByCustomer(customer);
			model.addAttribute("strAddress", customer);
		} else {
			shippingRate = shippingRateService.findByShippingRateByAddressDefault(address);
			model.addAttribute("strAddress", address);
		}
		
		CheckoutInfo checkoutInfo = checkoutService.getCheckoutInfo(listCartItems, shippingRate);
		
		model.addAttribute("checkoutInfo", checkoutInfo);
		model.addAttribute("listCartItems", listCartItems);
		
		float total = 0.0f;
		for (CartItem cartItem : listCartItems) {
			total += cartItem.getQuantity() * cartItem.getProduct().getPriceDiscountPercent();
		}
		model.addAttribute("total", total);
		
		return "/checkout/checkout";
	}
	
	
	private String formatCurrency(float amount) {
		String pattern = "###,###.##";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		return decimalFormat.format(amount);
	}
	
	private void sendCustomerVerify(HttpServletRequest request,Order order) 
			throws UnsupportedEncodingException, MessagingException {
		EmailSetting emailSetting = new EmailSetting(settingService.getEmail());
		
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSetting);
		
		mailSender.setDefaultEncoding("utf-8");
		
		String toAddress = order.getCustomer().getEmail();
		String subject = emailSetting.getCustomerOrderSubject();
		String content = emailSetting.getCustomerOrderContent();
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		//Change content subject
		subject = subject.replace("[[orderId]]", order.getId().toString());
		
		helper.setFrom(emailSetting.getFromAddress(), emailSetting.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		content = content.replace("[[name]]", order.getCustomer().getFullName());
		
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss E, dd MM yyyy");
		String dateOrder = dateFormat.format(order.getDateOrdertime());
		
		String totalAmount = formatCurrency(order.getTotal());
		
		content = content.replace("[[orderId]]", order.getId().toString());
		content = content.replace("[[orderTime]]", dateOrder);
		content = content.replace("[[shippingAddress]]", order.getAddress());
		content = content.replace("[[total]]", totalAmount);
		content = content.replace("[[methodPayment]]", order.getPaymentMethod().toString());
		
		helper.setText(content, true);
		
		mailSender.send(message);
	}
	
	@PostMapping("/place_order")
	public String viewPlaceOrder(HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		String paymentType = request.getParameter("payment");
		PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);
		
		String mail = getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getByEmail(mail);
		
		List<CartItem> listCartItems = cartItemService.listCartByCustomer(customer);
		
		ShippingRate shippingRate = null;
		Address address = addressService.getDefaultAddressByCustomer(customer);
	
		if (address == null) {
			shippingRate = shippingRateService.findShippingRateByCustomer(customer);
		} else {
			shippingRate = shippingRateService.findByShippingRateByAddressDefault(address);
		}
		
		CheckoutInfo checkoutInfo = checkoutService.getCheckoutInfo(listCartItems, shippingRate);
		
		Order order = orderService.createOrder(customer, address, listCartItems, paymentMethod, checkoutInfo);
		cartItemService.deleteByCustomer(customer);
		
		//Add status order
		OrderTrack orderTrack = new OrderTrack();
		orderTrack.setDateUpdate(new Date());
		orderTrack.setNotes(OrderStatus.NEW.defaultDescription());
		orderTrack.setOrderStatus(OrderStatus.NEW);
		orderTrack.setOrder(order);
		orderTrackService.save(orderTrack);
		
		sendCustomerVerify(request, order);
		
		return "/checkout/checkout_success";
	}
}
