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
    public CustomerResponse create(CustomerRequest r) {
        return repo.findByEmail(r.getEmail())
                .<CustomerResponse>map(x -> {
                    throw new IllegalStateException("Email already registered");
                })
                .orElseGet(() -> {
                    Customer c = Customer.builder()
                            .name(r.getName()).email(r.getEmail()).phone(r.getPhone())
                            .addressLine1(r.getAddressLine1()).addressLine2(r.getAddressLine2())
                            .city(r.getCity()).state(r.getState()).postalCode(r.getPostalCode())
                            .build();
                    return mapResponse(repo.save(c));
                });
    }


    @Override
    public CustomerResponse update(Long id, CustomerRequest r) {
        return repo.findById(id)
                .map(c -> {
                    c.setName(r.getName());
                    c.setPhone(r.getPhone());
                    c.setAddressLine1(r.getAddressLine1());
                    c.setAddressLine2(r.getAddressLine2());
                    c.setCity(r.getCity());
                    c.setState(r.getState());
                    c.setPostalCode(r.getPostalCode());
                    return mapResponse(repo.save(c));
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

    private CustomerResponse mapResponse(Customer c) {
        return CustomerResponse.builder()
                .id(c.getId()).name(c.getName()).email(c.getEmail()).phone(c.getPhone())
                .addressLine1(c.getAddressLine1()).addressLine2(c.getAddressLine2())
                .city(c.getCity()).state(c.getState()).postalCode(c.getPostalCode())
                .build();
    }

}
