package com.ecommerce.project.serviceimpl;

import com.ecommerce.project.entity.Customer;
import com.ecommerce.project.entity.Order;
import com.ecommerce.project.entity.OrderItem;
import com.ecommerce.project.entity.Product;
import com.ecommerce.project.enumpackage.OrderStatus;
import com.ecommerce.project.repository.CustomerRepository;
import com.ecommerce.project.repository.OrderRepository;
import com.ecommerce.project.repository.ProductRepository;
import com.ecommerce.project.request.OrderCreateRequest;
import com.ecommerce.project.request.OrderShippingUpdateRequest;
import com.ecommerce.project.response.OrderResponse;
import com.ecommerce.project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final CustomerRepository customerRepo;
    private final ProductRepository productRepo;


    @Override
    public OrderResponse create(OrderCreateRequest r) {
        Customer customer = customerRepo.findById(r.getCustomerId())
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));

        Order order = Order.builder()
                .customer(customer)
                .shippingAddressLine1(r.getShippingAddressLine1())
                .shippingAddressLine2(r.getShippingAddressLine2())
                .shippingCity(r.getShippingCity())
                .shippingState(r.getShippingState())
                .shippingPostalCode(r.getShippingPostalCode())
                .status(OrderStatus.NEW)
                .build();

        double total = 0.0;
        List<OrderItem> items = new ArrayList<>();

        for (OrderCreateRequest.Item it : r.getItems()) {
            Product p = productRepo.findById(it.getProductId())
                    .orElseThrow(() -> new NoSuchElementException("Product not found: " + it.getProductId()));

            if (p.getQuantity() < it.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + p.getTitle());
            }

            p.setQuantity(p.getQuantity() - it.getQuantity());

            double lineTotal = p.getPrice() * it.getQuantity();

            OrderItem oi = OrderItem.builder()
                    .order(order)
                    .product(p)
                    .quantity(it.getQuantity())
                    .unitPrice(p.getPrice())
                    .lineTotal(lineTotal)
                    .build();

            items.add(oi);
            total += lineTotal;
        }

        order.setItems(items);
        order.setTotalAmount(total);

        return mapResponse(orderRepo.save(order));
    }

    @Override
    public OrderResponse cancel(Long orderId) {
        Order o = orderRepo.findById(orderId).orElseThrow(() -> new NoSuchElementException("Order not found"));
        if (o.getStatus() == OrderStatus.CANCELLED) return mapResponse(o);
        if (o.getStatus() == OrderStatus.SHIPPED || o.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel shipped or delivered order");
        }
        for (OrderItem oi : o.getItems()) {
            Product p = oi.getProduct();
            p.setQuantity(p.getQuantity() + oi.getQuantity());
        }
        o.setStatus(OrderStatus.CANCELLED);
        return mapResponse(orderRepo.save(o));
    }

    @Override
    public OrderResponse updateShipping(Long orderId, OrderShippingUpdateRequest s) {
        Order o = orderRepo.findById(orderId).orElseThrow(() -> new NoSuchElementException("Order not found"));
        if (o.getStatus() == OrderStatus.SHIPPED || o.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot update shipping after shipment");
        }
        if (s.getShippingAddressLine1() != null) o.setShippingAddressLine1(s.getShippingAddressLine1());
        o.setShippingAddressLine2(s.getShippingAddressLine2());
        o.setShippingCity(s.getShippingCity());
        o.setShippingState(s.getShippingState());
        o.setShippingPostalCode(s.getShippingPostalCode());
        return mapResponse(orderRepo.save(o));
    }

    @Override
    public OrderResponse get(Long id) {
        return mapResponse(orderRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Order not found")));
    }

    private OrderResponse mapResponse(Order o){
        var items = o.getItems().stream().map(oi -> OrderResponse.Item.builder()
                .productId(oi.getProduct().getId())
                .title(oi.getProduct().getTitle())
                .quantity(oi.getQuantity())
                .unitPrice(oi.getUnitPrice())
                .lineTotal(oi.getLineTotal())
                .build()).toList();

        return OrderResponse.builder()
                .id(o.getId()).status(o.getStatus().name())
                .customerId(o.getCustomer().getId())
                .items(items).totalAmount(o.getTotalAmount())
                .shippingAddressLine1(o.getShippingAddressLine1())
                .shippingAddressLine2(o.getShippingAddressLine2())
                .shippingCity(o.getShippingCity())
                .shippingState(o.getShippingState())
                .shippingPostalCode(o.getShippingPostalCode())
                .build();
    }
}
