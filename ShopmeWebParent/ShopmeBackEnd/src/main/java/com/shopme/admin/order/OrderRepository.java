package com.shopme.admin.order;


import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	@Query("SELECT o FROM Order o WHERE CONCAT('#', o.id) LIKE %?1% OR o.firstName LIKE %?1% OR"
			+ " o.lastName LIKE %?1% OR o.phoneNumber LIKE %?1% OR"
			+ " o.addressLine1 LIKE %?1% OR o.addressLine2 LIKE %?1% OR"
			+ " o.postalCode LIKE %?1% OR o.city LIKE %?1% OR"
			+ " o.state.name LIKE %?1% OR"
			+ " o.paymentMethod LIKE %?1% OR o.orderStatus LIKE %?1% OR"
			+ " o.customer.firstName LIKE %?1% OR"
			+ " o.customer.lastName LIKE %?1%")
	public Page<Order> findAll(String keyword, Pageable pageable);
	
	public Long countById(Integer id);
	
	@Modifying
	@Query("UPDATE Order o SET o.orderStatus = ?2 WHERE o.id = ?1")
	public void updateStatus(Integer orderId, OrderStatus orderStatus);
	
	@Query("SELECT NEW com.shopme.common.entity.Order(o.id, o.dateOrdertime, o.total"
			+ " ) FROM Order o WHERE"
			+ " o.dateOrdertime BETWEEN ?1 AND ?2 ORDER BY o.dateOrdertime ASC")
	public List<Order> findByOrderTimeBetween(Date startTime, Date endTime);
}
