package com.ecommerce.project.serviceimpl;

import com.ecommerce.project.entity.Customer;
import com.ecommerce.project.repository.CustomerRepository;
import com.ecommerce.project.request.CustomerRequest;
import com.ecommerce.project.response.CustomerResponse;
import com.ecommerce.project.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repo;

    @Override
    public CustomerResponse create(CustomerRequest request) {
        return repo.findByEmail(request.getEmail())
                .<CustomerResponse>map(x -> {
                    throw new IllegalStateException("Email already registered");
                })
                .orElseGet(() -> {
                    Customer customer = Customer.builder()
                            .name(request.getName()).email(request.getEmail()).phone(request.getPhone())
                            .addressLine1(request.getAddressLine1()).addressLine2(request.getAddressLine2())
                            .city(request.getCity()).state(request.getState()).postalCode(request.getPostalCode())
                            .build();
                    return mapResponse(repo.save(customer));
                });
    }


    @Override
    public CustomerResponse update(Long id, CustomerRequest customerRequest) {
        return repo.findById(id)
                .map(customer -> {
                    customer.setName(customerRequest.getName());
                    customer.setPhone(customerRequest.getPhone());
                    customer.setAddressLine1(customerRequest.getAddressLine1());
                    customer.setAddressLine2(customerRequest.getAddressLine2());
                    customer.setCity(customerRequest.getCity());
                    customer.setState(customerRequest.getState());
                    customer.setPostalCode(customerRequest.getPostalCode());
                    return mapResponse(repo.save(customer));
                })
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));
    }

    @Override
    public CustomerResponse get(Long id) {
        return repo.findById(id)
                .map(this::mapResponse)
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));
    }

    @Override
    public List<CustomerResponse> list() {
        return repo.findAll().stream()
                .map(this::mapResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    private CustomerResponse mapResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId()).name(customer.getName()).email(customer.getEmail()).phone(customer.getPhone())
                .addressLine1(customer.getAddressLine1()).addressLine2(customer.getAddressLine2())
                .city(customer.getCity()).state(customer.getState()).postalCode(customer.getPostalCode())
                .build();
    }

}
