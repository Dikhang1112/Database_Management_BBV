package controllers;

import dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pojo.Customer;
import services.CustomerService;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "CRUD APIs for managing customers (companies/tenants)")
@SecurityRequirement(name = "Bearer Authentication")
public class CustomerController {

    private final CustomerService customerService;

    // =====================================================
    // GET ALL / SEARCH CUSTOMERS (PAGINATED)
    // =====================================================

    @Operation(summary = "Get paginated customers list", description = "Retrieve paginated list of customers (default 5 items per page), with optional search filters")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved paginated customers list",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class),
                            examples = @ExampleObject(
                                    name = "CustomersPageExample",
                                    summary = "Sample paginated customers response",
                                    value = """
                                            {
                                              "data": [
                                                {
                                                  "id": 1,
                                                  "companyName": "Figma",
                                                  "website": "https://figma.com",
                                                  "product": "Design Tools",
                                                  "description": "Collaborative interface design platform.",
                                                  "status": "ACTIVE",
                                                  "createdAt": "15/01/2026 09:00:00",
                                                  "updatedAt": "20/07/2026 15:30:00"
                                                }
                                              ],
                                              "page": 1,
                                              "size": 5,
                                              "totalElements": 10,
                                              "totalPages": 2,
                                              "last": false
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<PageResponse<Customer>> getAllCustomers
            (@Parameter(description = "Filter by company name") @RequestParam(required = false) String companyName, @Parameter(description = "Filter by product category")
    @RequestParam(required = false) String product, @Parameter(description = "Page number (default 1)")
    @RequestParam(defaultValue = "1") int page, @Parameter(description = "Page size (default 5)")
    @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(customerService.findPaginated(companyName, product, page, size));
    }

    // =====================================================
    // GET CUSTOMER BY ID
    // =====================================================

    @Operation(summary = "Get customer by ID", description = "Retrieve single customer details by customer ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved customer details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class),
                            examples = @ExampleObject(
                                    name = "CustomerDetailExample",
                                    summary = "Sample customer detail",
                                    value = """
                                            {
                                              "id": 1,
                                              "companyName": "Figma",
                                              "website": "https://figma.com",
                                              "product": "Design Tools",
                                              "description": "Collaborative interface design platform.",
                                              "status": "ACTIVE",
                                              "createdAt": "15/01/2026 09:00:00",
                                              "updatedAt": "20/07/2026 15:30:00"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@Parameter(description = "Customer ID") @PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    // =====================================================
    // CREATE CUSTOMER
    // =====================================================

    @Operation(summary = "Create a new customer", description = "Add a new customer company to the system")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Customer created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class),
                            examples = @ExampleObject(
                                    name = "CreatedCustomerExample",
                                    summary = "Sample created customer response",
                                    value = """
                                            {
                                              "id": 11,
                                              "companyName": "Vercel",
                                              "website": "https://vercel.com",
                                              "product": "Cloud Platform",
                                              "description": "Frontend cloud platform.",
                                              "status": "ACTIVE",
                                              "createdAt": "02/08/2026 17:00:00",
                                              "updatedAt": "02/08/2026 17:00:00"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Customer details payload", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class), examples = @ExampleObject(name = "CreateCustomerRequestExample", value = """
            {
              "companyName": "Vercel",
              "website": "https://vercel.com",
              "product": "Cloud Platform",
              "description": "Frontend cloud platform.",
              "status": "ACTIVE"
            }
            """))) @RequestBody Customer customer) {
        Customer createdCustomer = customerService.create(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    // =====================================================
    // UPDATE CUSTOMER
    // =====================================================

    @Operation(summary = "Update customer by ID", description = "Modify an existing customer company details")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class),
                            examples = @ExampleObject(
                                    name = "UpdatedCustomerExample",
                                    summary = "Sample updated customer response",
                                    value = """
                                            {
                                              "id": 1,
                                              "companyName": "Figma Inc",
                                              "website": "https://figma.com",
                                              "product": "Design Tools",
                                              "description": "Collaborative design platform.",
                                              "status": "ACTIVE",
                                              "createdAt": "15/01/2026 09:00:00",
                                              "updatedAt": "02/08/2026 17:05:00"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@Parameter(description = "Customer ID") @PathVariable Long id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated customer payload", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class), examples = @ExampleObject(name = "UpdateCustomerRequestExample", value = """
            {
              "companyName": "Figma Inc",
              "website": "https://figma.com",
              "product": "Design Tools",
              "description": "Collaborative design platform.",
              "status": "ACTIVE"
            }
            """))) @RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.update(id, customer));
    }

    // =====================================================
    // DELETE CUSTOMER
    // =====================================================

    @Operation(summary = "Delete customer by ID", description = "Remove a customer company by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@Parameter(description = "Customer ID") @PathVariable Long id) {
        boolean deleted = customerService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
