package com.appsys.finance.service;

import com.appsys.finance.entity.Customer;
import com.appsys.finance.repository.CustomerRepository;
import com.appsys.finance.util.AesEncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AesEncryptUtil aesEncryptUtil;

    public Customer createCustomer(Customer customer) {
        if (customer.getPhone() != null && !customer.getPhone().isEmpty()) {
            customer.setPhone(aesEncryptUtil.encrypt(customer.getPhone()));
        }
        if (customer.getEmail() != null && !customer.getEmail().isEmpty()) {
            customer.setEmail(aesEncryptUtil.encrypt(customer.getEmail()));
        }
        return customerRepository.save(customer);
    }

    public Optional<Customer> getCustomerById(Long id) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        customerOpt.ifPresent(this::decryptCustomer);
        return customerOpt;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll().stream()
            .peek(this::decryptCustomer)
            .collect(Collectors.toList());
    }

    @Transactional
    public Customer updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id).map(customer -> {
            customer.setName(customerDetails.getName());
            if (customerDetails.getPhone() != null && !customerDetails.getPhone().isEmpty()) {
                customer.setPhone(aesEncryptUtil.encrypt(customerDetails.getPhone()));
            }
            if (customerDetails.getEmail() != null && !customerDetails.getEmail().isEmpty()) {
                customer.setEmail(aesEncryptUtil.encrypt(customerDetails.getEmail()));
            }
            customer.setAddress(customerDetails.getAddress());
            customer.setRemark(customerDetails.getRemark());
            return customerRepository.save(customer);
        }).orElseThrow(() -> new RuntimeException("客户不存在，ID: " + id));
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    private void decryptCustomer(Customer customer) {
        if (customer.getPhone() != null) {
            customer.setPhone(aesEncryptUtil.decrypt(customer.getPhone()));
        }
        if (customer.getEmail() != null) {
            customer.setEmail(aesEncryptUtil.decrypt(customer.getEmail()));
        }
    }
}
