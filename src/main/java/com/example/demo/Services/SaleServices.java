package com.example.demo.Services;

import com.example.demo.POJO.SaleEvent;
import com.example.demo.entity.Car;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Sale;
import com.example.demo.entity.SalesPerson;
import com.example.demo.Exception.ResourceNotFoundException;
import com.example.demo.Repositories.CarRepository;
import com.example.demo.Repositories.CustomerRepository;
import com.example.demo.Repositories.SaleRepository;
import com.example.demo.Repositories.SalespersonRepository;
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
import java.time.LocalDate;
import java.util.List;

@Service
public class SaleServices {

    private static final Logger logger = LoggerFactory.getLogger(SaleServices.class);

    @Autowired
    private SaleRepository salerepo;

    @Autowired
    private CustomerRepository customerrepo;

    @Autowired
    private CarRepository carrepo;

    @Autowired
    private SalespersonRepository salespersonrepo;

    @Autowired
    private RabbitMQPublisher publisher;

    public Page<Sale> getSalesPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        logger.info("Fetching all Sales sorted by: " + sortBy);
        return salerepo.findAll(pageable);
    }


    public List<Sale> getsalesrecord(){
        logger.info("Fetching all sales records");
        return salerepo.findAll();
    }
    @Cacheable(value = "sales", key = "#id")
    public Sale getsalebyid(int id){
        logger.info("Fetching sale record by id: {}", id);
        return salerepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Sale record not found with id: {}", id);
                    return new ResourceNotFoundException("Sale Record with id " + id + " not found");
                });
    }

    @CacheEvict(value = "sales", key = "#id")
    public void deletesalerecord(int id){
        Sale sale= salerepo.findById(id).orElseThrow(() -> new RuntimeException("Sale not found with ID: " + id));
        logger.info("Deleting sale record with id: {}", id);
        SaleEvent event= new SaleEvent();
        event.setEventType("SALE_DELETED");
        event.setId(sale.getId());
        event.setDate(sale.getDate());
        publisher.sendSaleEvent(event);
        salerepo.deleteById(id);
        logger.info("Deleted sale record with id: {}", id);
    }



    @CachePut(value = "sales", key = "#result.id")
    public Sale createnewsalerecord(int carid, int customerid, int salespersonid, double pricesold){
        logger.info("Creating new sale record for carId: {}, customerId: {}, salespersonId: {}", carid, customerid, salespersonid);
        Car car = carrepo.findById(carid).orElseThrow(() -> new ResourceNotFoundException("Car with id " + carid + " not found"));
        Customer customer = customerrepo.findById(customerid).orElseThrow(() -> new ResourceNotFoundException("Customer with id " + customerid + " not found"));
        SalesPerson saleperson = salespersonrepo.findById(salespersonid).orElseThrow(() -> new ResourceNotFoundException("Salesperson with id " + salespersonid + " not found"));
        Sale sale= new Sale();
        sale.setDate(LocalDate.now());
        sale.setPricesold(pricesold);
        sale.setCar(car);
        sale.setCustomer(customer);
        sale.setSalesperson(saleperson);
        salerepo.save(sale);

        SaleEvent event= new SaleEvent();
        event.setEventType("SALE_CREATED");
        event.setId(sale.getId());
        event.setDate(sale.getDate());
        publisher.sendSaleEvent(event);
        return sale;
    }


    @CachePut(value = "sales", key = "#result.id")
    public Sale updatesalerecordinfo(int id, int carid, int customerid, int salepersonid, double pricesold){
        logger.info("Updating sale record id: {}", id);
        Sale oldsalerecord = salerepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale Record with id " + id + " not found"));
        Car car = carrepo.findById(carid).orElseThrow(() -> new ResourceNotFoundException("Car with id " + carid + " not found"));
        Customer customer = customerrepo.findById(customerid).orElseThrow(() -> new ResourceNotFoundException("Customer with id " + customerid + " not found"));
        SalesPerson salesPerson = salespersonrepo.findById(salepersonid).orElseThrow(() -> new ResourceNotFoundException("Salesperson with id " + salepersonid + " not found"));

        oldsalerecord.setDate(LocalDate.now());
        oldsalerecord.setPricesold(pricesold);
        oldsalerecord.setCar(car);
        oldsalerecord.setCustomer(customer);
        oldsalerecord.setSalesperson(salesPerson);

        SaleEvent event= new SaleEvent();
        event.setEventType("SALE_UPDATED");
        event.setId(oldsalerecord.getId());
        event.setDate(oldsalerecord.getDate());
        publisher.sendSaleEvent(event);
        return salerepo.save(oldsalerecord);
    }
}
