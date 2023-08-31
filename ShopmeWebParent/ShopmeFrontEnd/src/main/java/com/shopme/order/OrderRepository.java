package com.shopme.order;

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
	@Query("SELECT o FROM Order o WHERE o.customer.id = ?2 AND (CONCAT('#', o.id) LIKE %?1% "
			+ "OR o.firstName LIKE %?1%)")
	public Page<Order> findAll(String keyword, Integer customerID, Pageable pageable);
	
	public Long countById(Integer id);
	
	@Modifying
	@Query("UPDATE Order o SET o.orderStatus = ?2 WHERE o.id = ?1")
	public void updateStatus(Integer orderId, OrderStatus orderStatus);
}
