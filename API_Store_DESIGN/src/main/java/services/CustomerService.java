package services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pojo.Customer;
import repositories.CustomerRepository;
import repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    // =====================================================
    // CRUD Operations
    // =====================================================

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    public List<Customer> search(String companyName, String product) {
        return customerRepository.search(companyName, product);
    }

    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer update(Long id, Customer customer) {
        return customerRepository.update(id, customer)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    public boolean delete(Long id) {
        return customerRepository.deleteById(id);
    }

    // =====================================================
    // Statistics & Aggregations
    // =====================================================

    public long countTotalCustomers() {
        return customerRepository.countTotalCustomers();
    }

    public long countTotalUsers() {
        return userRepository.countTotalUsers();
    }

    public long countTotalActiveUsers() {
        return userRepository.countTotalActiveUsers();
    }
}
