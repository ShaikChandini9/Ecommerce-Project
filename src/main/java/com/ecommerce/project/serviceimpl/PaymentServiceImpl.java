package com.ecommerce.project.serviceimpl;

import com.ecommerce.project.entity.Order;
import com.ecommerce.project.entity.Payment;
import com.ecommerce.project.enumpackage.OrderStatus;
import com.ecommerce.project.enumpackage.PaymentStatus;
import com.ecommerce.project.repository.OrderRepository;
import com.ecommerce.project.repository.PaymentRepository;
import com.ecommerce.project.request.PaymentRequest;
import com.ecommerce.project.response.PaymentResponse;
import com.ecommerce.project.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepo;
    private final OrderRepository orderRepo;

    @Override
    public PaymentResponse pay(PaymentRequest r) {
        Order order = orderRepo.findById(r.getOrderId())
                .orElseThrow(() -> new NoSuchElementException("Order not found"));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot pay for cancelled order");
        }

        Double orderTotal = order.getTotalAmount();

        if (r.getAmount() == null || !r.getAmount().equals(orderTotal)) {
            throw new IllegalArgumentException("Payment failed: Must pay the full order amount (" + orderTotal + ")");
        }

        Payment p = Payment.builder()
                .order(order)
                .mode(r.getMode())
                .amount(orderTotal) // always trust server-side total
                .confirmationCode(generateConfirmationCode(order.getId()))
                .status(PaymentStatus.CONFIRMED)
                .build();

        order.setStatus(OrderStatus.PAID);
        p = paymentRepo.save(p);

        return PaymentResponse.builder()
                .id(p.getId())
                .orderId(order.getId())
                .mode(p.getMode().name())
                .status(p.getStatus().name())
                .confirmationCode(p.getConfirmationCode())
                .amount(p.getAmount())
                .createdAt(p.getCreatedAt())
                .build();
    }

    private String generateConfirmationCode(Long orderId) {
        return "ORD-" + orderId + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public Double getTotalPaidByCustomer(Long customerId) {
        return paymentRepo.findByOrderCustomerIdAndStatus(customerId, PaymentStatus.CONFIRMED)
                .stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }
}
