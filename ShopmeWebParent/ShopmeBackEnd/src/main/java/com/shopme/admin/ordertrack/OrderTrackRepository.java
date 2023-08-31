package com.shopme.admin.ordertrack;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.OrderTrack;

@Repository
public interface OrderTrackRepository extends JpaRepository<OrderTrack, Integer>{
}
