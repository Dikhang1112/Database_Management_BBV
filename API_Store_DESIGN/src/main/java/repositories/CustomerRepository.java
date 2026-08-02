package repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import dto.PageResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import pojo.Customer;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepository {

    private static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    private final List<Customer> customers = new ArrayList<>();
    private final ObjectMapper objectMapper;

    public CustomerRepository() {
        objectMapper = new ObjectMapper();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        objectMapper.registerModule(javaTimeModule);
    }

    @PostConstruct
    public void loadMockData() {
        try (InputStream inputStream = new org.springframework.core.io.ClassPathResource("data/customers.json").getInputStream()) {
            List<Customer> mockCustomers = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<Customer>>() {}
            );
            customers.clear();
            customers.addAll(mockCustomers);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data/customers.json", e);
        }
    }

    public List<Customer> findAll() {
        return new ArrayList<>(customers);
    }

    public Optional<Customer> findById(Long id) {
        return customers.stream()
                .filter(cust -> cust.getId().equals(id))
                .findFirst();
    }

    public List<Customer> search(String companyName, String product) {
        return customers.stream()
                .filter(customer ->
                        contains(customer.getCompanyName(), companyName)
                                && contains(customer.getProduct(), product)
                )
                .toList();
    }

    public PageResponse<Customer> findPaginated(String companyName, String product, int page, int size) {
        List<Customer> filteredList = search(companyName, product);
        int pageNum = Math.max(1, page);
        int pageSize = size <= 0 ? 5 : size;
        int totalElements = filteredList.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        int fromIndex = (pageNum - 1) * pageSize;
        List<Customer> pageContent;
        if (fromIndex >= totalElements) {
            pageContent = Collections.emptyList();
        } else {
            int toIndex = Math.min(fromIndex + pageSize, totalElements);
            pageContent = filteredList.subList(fromIndex, toIndex);
        }

        return PageResponse.<Customer>builder()
                .data(pageContent)
                .page(pageNum)
                .size(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .last(pageNum >= totalPages)
                .build();
    }

    private boolean contains(String value, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }
        return value != null
                && value.toLowerCase()
                .contains(keyword.trim().toLowerCase());
    }

    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            long maxId = customers.stream().mapToLong(Customer::getId).max().orElse(0L);
            customer.setId(maxId + 1);
        }
        if (customer.getCreatedAt() == null) {
            customer.setCreatedAt(LocalDateTime.now());
        }
        customer.setUpdatedAt(LocalDateTime.now());
        customers.add(customer);
        return customer;
    }

    public Optional<Customer> update(Long id, Customer updatedCustomer) {
        Optional<Customer> existingOpt = findById(id);
        if (existingOpt.isPresent()) {
            Customer existing = existingOpt.get();
            if (updatedCustomer.getCompanyName() != null) existing.setCompanyName(updatedCustomer.getCompanyName());
            if (updatedCustomer.getWebsite() != null) existing.setWebsite(updatedCustomer.getWebsite());
            if (updatedCustomer.getProduct() != null) existing.setProduct(updatedCustomer.getProduct());
            if (updatedCustomer.getDescription() != null) existing.setDescription(updatedCustomer.getDescription());
            if (updatedCustomer.getStatus() != null) existing.setStatus(updatedCustomer.getStatus());
            existing.setUpdatedAt(LocalDateTime.now());
            return Optional.of(existing);
        }
        return Optional.empty();
    }

    public boolean deleteById(Long id) {
        return customers.removeIf(cust -> cust.getId().equals(id));
    }

    public long countTotalCustomers() {
        return customers.size();
    }
}
