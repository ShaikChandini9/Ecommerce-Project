package com.ecommerce.project.repository;

import com.ecommerce.project.entity.OrderItem;
import com.ecommerce.project.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByProduct(Product product);
}
