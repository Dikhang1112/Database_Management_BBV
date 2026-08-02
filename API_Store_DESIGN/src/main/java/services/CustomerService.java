package services;

import dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pojo.Customer;
import repositories.CustomerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    // =====================================================
    // CRUD & Search Operations
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

    public PageResponse<Customer> findPaginated(String companyName, String product, int page, int size) {
        return customerRepository.findPaginated(companyName, product, page, size);
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
}
