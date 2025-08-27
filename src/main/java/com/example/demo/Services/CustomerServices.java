package com.example.demo.Services;

import com.example.demo.POJO.CustomerEvent;
import com.example.demo.entity.Car;
import com.example.demo.entity.Customer;
import com.example.demo.Exception.ResourceNotFoundException;
import com.example.demo.Repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.demo.messaging.RabbitMQPublisher;

import java.util.List;

@Service
public class CustomerServices {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServices.class);

    @Autowired
    private CustomerRepository customerrepo;

    @Autowired
    private RabbitMQPublisher publisher;

    public Page<Customer> getCustomersPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        logger.info("Fetching all Customers sorted by: " + sortBy);
        return customerrepo.findAll(pageable);
    }


    public List<Customer> getallcustomer(){
        logger.info("Fetching all customers");
        return customerrepo.findAll();
    }
    @Cacheable(value = "customers", key = "#id")
    public Customer getcustomerbyid(int id){
        logger.info("Fetching customer by id: {}", id);
        return customerrepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Customer not found with id: {}", id);
                    return new ResourceNotFoundException("Customer with id " + id + " not found");
                });
    }
    @CachePut(value = "customers", key ="#result.id")
    public Customer createnewcustomer(Customer customer){
        logger.info("Creating new customer: {}", customer.getName());
        customerrepo.save(customer);
        CustomerEvent event= new CustomerEvent();
        event.setEventType("CUSTOMER_CREATED");
        event.setId(customer.getId());
        event.setName(customer.getName());
        event.setPhone(customer.getPhone());
        publisher.sendCustomerEvent(event);
        return customer;
    }

    @CacheEvict(value = "customers", key = "#id")
    public void deletecustomer(int id){
        Customer customer = customerrepo.findById(id).orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        logger.info("Deleting customer with id: {}", id);
        CustomerEvent event= new CustomerEvent();
        event.setEventType("CUSTOMER_DELETED");
        event.setId(customer.getId());
        event.setName(customer.getName());
        event.setPhone(customer.getPhone());
        publisher.sendCustomerEvent(event);
        customerrepo.deleteById(id);
        logger.info("Deleted customer with id: {}", id);
    }

    @CachePut(value = "customers", key = "#result.id")
    public Customer updatecustomerdetails(int id, Customer newcustomer){
        logger.info("Updating customer with id: {}", id);
        Customer oldcustomer = customerrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id " + id + " not found"));
        oldcustomer.setName(newcustomer.getName());
        oldcustomer.setEmail(newcustomer.getEmail());
        oldcustomer.setPhone(newcustomer.getPhone());
        oldcustomer.setSales(newcustomer.getSales());
        CustomerEvent event=new CustomerEvent();
        event.setEventType("CUSTOMER_UPDATED");
        event.setId(oldcustomer.getId());
        event.setName(oldcustomer.getName());
        event.setPhone(oldcustomer.getPhone());
        publisher.sendCustomerEvent(event);
        return customerrepo.save(oldcustomer);
    }
}
