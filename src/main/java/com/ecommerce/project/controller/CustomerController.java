package com.ecommerce.project.controller;

import com.ecommerce.project.request.CustomerRequest;
import com.ecommerce.project.response.CustomerResponse;
import com.ecommerce.project.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @PostMapping( "/create-user")
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<CustomerResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<CustomerResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PutMapping("/update-by-id/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable Long id, @Valid @RequestBody CustomerRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Customer deleted");
    }
}
