package com.shopme.admin.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderDetail;
import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.OrderTrack;
import com.shopme.common.entity.PaymentMethod;
import com.shopme.common.entity.Product;

import jakarta.persistence.EntityManager;

@DataJpaTest()
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderRepositoryTest {
	@Autowired
	private OrderRepository repo;
	
	@Autowired
	private EntityManager entityManager;
	
	@Test
	public void testCreateNewOrderWithSingleProduct() {
		Customer customer = entityManager.find(Customer.class, 29);
		Product product = entityManager.find(Product.class, 1);
		

		Order mainOrder = new Order();
		mainOrder.setDateOrdertime(new Date());
		mainOrder.setCustomer(customer);
		mainOrder.copyAddressFromCustomer();

		mainOrder.setShippingCost(10);
		mainOrder.setTotal((float)(product.getPrice() + 10));

		mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
		mainOrder.setOrderStatus(OrderStatus.NEW);
		mainOrder.setDeliverDate(new Date());
		mainOrder.setDeliverDays(1);

		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setProduct(product);
		orderDetail.setOrder(mainOrder);
		orderDetail.setProductCost((float)product.getCost());
		orderDetail.setShippingCost(10);
		orderDetail.setQuantity(1);

		mainOrder.getOrderDetails().add(orderDetail);

		Order savedOrder = repo.save(mainOrder);

		assertThat(savedOrder.getId()).isGreaterThan(0);		
	}
	
	@Test
	public void testCreateNewOrderWithMultipleProducts() {
		Customer customer = entityManager.find(Customer.class, 26);
		Product product1 = entityManager.find(Product.class, 25);
		Product product2 = entityManager.find(Product.class, 26);

		Order mainOrder = new Order();
		mainOrder.setDateOrdertime(new Date());
		mainOrder.setCustomer(customer);
		mainOrder.copyAddressFromCustomer();

		OrderDetail orderDetail1 = new OrderDetail();
		orderDetail1.setProduct(product1);
		orderDetail1.setOrder(mainOrder);
		orderDetail1.setProductCost((float)product1.getCost());
		orderDetail1.setShippingCost(10);
		orderDetail1.setQuantity(1);
		orderDetail1.setProductCost((float)product1.getPrice());

		OrderDetail orderDetail2 = new OrderDetail();
		orderDetail2.setProduct(product2);
		orderDetail2.setOrder(mainOrder);
		orderDetail2.setProductCost((float)product2.getCost());
		orderDetail2.setShippingCost(20);
		orderDetail2.setQuantity(2);
		orderDetail2.setProductCost((float)product2.getPrice() * 2);

		mainOrder.getOrderDetails().add(orderDetail1);
		mainOrder.getOrderDetails().add(orderDetail2);

		mainOrder.setShippingCost(30);
		mainOrder.setTotal((float)(product1.getPriceDiscountPercent() + 2 * product2.getPriceDiscountPercent()));

		mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
		mainOrder.setOrderStatus(OrderStatus.PACKAGED);
		mainOrder.setDeliverDate(new Date());
		mainOrder.setDeliverDays(3);

		Order savedOrder = repo.save(mainOrder);		
		assertThat(savedOrder.getId()).isGreaterThan(0);		
	}
	
	@Test
	public void testUpdateOrderTracks() {
		Integer orderId = 6;
		Order order = repo.findById(orderId).get();

		OrderTrack newTrack = new OrderTrack();
		newTrack.setOrder(order);
		newTrack.setDateUpdate(new Date());
		newTrack.setOrderStatus(OrderStatus.NEW);
		newTrack.setNotes(OrderStatus.NEW.defaultDescription());

		OrderTrack processingTrack = new OrderTrack();
		processingTrack.setOrder(order);
		processingTrack.setDateUpdate(new Date());
		processingTrack.setOrderStatus(OrderStatus.PROCESSING);
		processingTrack.setNotes(OrderStatus.PROCESSING.defaultDescription());

		List<OrderTrack> orderTracks = order.getOrderTracks();
		orderTracks.add(newTrack);
		orderTracks.add(processingTrack);

		Order updatedOrder = repo.save(order);

		assertThat(updatedOrder.getOrderTracks()).hasSizeGreaterThan(1);
	}
	
}
