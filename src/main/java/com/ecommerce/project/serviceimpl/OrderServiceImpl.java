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
    public OrderResponse create(OrderCreateRequest request) {
        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));

        Order order = Order.builder()
                .customer(customer)
                .shippingAddressLine1(request.getShippingAddressLine1())
                .shippingAddressLine2(request.getShippingAddressLine2())
                .shippingCity(request.getShippingCity())
                .shippingState(request.getShippingState())
                .shippingPostalCode(request.getShippingPostalCode())
                .status(OrderStatus.NEW)
                .build();

        double total = 0.0;
        List<OrderItem> items = new ArrayList<>();

        for (OrderCreateRequest.Item item : request.getItems()) {
            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new NoSuchElementException("Product not found: " + item.getProductId()));

            if (product.getQuantity() < item.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getTitle());
            }

            product.setQuantity(product.getQuantity() - item.getQuantity());

            double lineTotal = product.getPrice() * item.getQuantity();

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(item.getQuantity())
                    .unitPrice(product.getPrice())
                    .lineTotal(lineTotal)
                    .build();

            items.add(orderItem);
            total += lineTotal;
        }

        order.setItems(items);
        order.setTotalAmount(total);

        return mapResponse(orderRepo.save(order));
    }

    @Override
    public OrderResponse cancel(Long orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new NoSuchElementException("Order not found"));
        if (order.getStatus() == OrderStatus.CANCELLED) return mapResponse(order);
        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel shipped or delivered order");
        }
        for (OrderItem orderItem : order.getItems()) {
            Product p = orderItem.getProduct();
            p.setQuantity(p.getQuantity() + orderItem.getQuantity());
        }
        order.setStatus(OrderStatus.CANCELLED);
        return mapResponse(orderRepo.save(order));
    }

    @Override
    public OrderResponse updateShipping(Long orderId, OrderShippingUpdateRequest shippingUpdateRequest) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new NoSuchElementException("Order not found"));
        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot update shipping after shipment");
        }
        if (shippingUpdateRequest.getShippingAddressLine1() != null) order.setShippingAddressLine1(shippingUpdateRequest.getShippingAddressLine1());
        order.setShippingAddressLine2(shippingUpdateRequest.getShippingAddressLine2());
        order.setShippingCity(shippingUpdateRequest.getShippingCity());
        order.setShippingState(shippingUpdateRequest.getShippingState());
        order.setShippingPostalCode(shippingUpdateRequest.getShippingPostalCode());
        return mapResponse(orderRepo.save(order));
    }

    @Override
    public OrderResponse get(Long id) {
        return mapResponse(orderRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Order not found")));
    }

    private OrderResponse mapResponse(Order order){
        var items = order.getItems().stream().map(orderItem -> OrderResponse.Item.builder()
                .productId(orderItem.getProduct().getId())
                .title(orderItem.getProduct().getTitle())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .lineTotal(orderItem.getLineTotal())
                .build()).toList();

        return OrderResponse.builder()
                .id(order.getId()).status(order.getStatus().name())
                .customerId(order.getCustomer().getId())
                .items(items).totalAmount(order.getTotalAmount())
                .shippingAddressLine1(order.getShippingAddressLine1())
                .shippingAddressLine2(order.getShippingAddressLine2())
                .shippingCity(order.getShippingCity())
                .shippingState(order.getShippingState())
                .shippingPostalCode(order.getShippingPostalCode())
                .build();
    }
}
