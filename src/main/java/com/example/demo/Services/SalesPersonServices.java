package com.example.demo.Services;

import com.example.demo.POJO.SalesPersonEvent;
import com.example.demo.entity.Car;
import com.example.demo.entity.SalesPerson;
import com.example.demo.Exception.ResourceNotFoundException;
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
import java.util.List;

@Service
public class SalesPersonServices {

    private static final Logger logger = LoggerFactory.getLogger(SalesPersonServices.class);

    @Autowired
    private SalespersonRepository salespersonrepo;

    @Autowired
    private RabbitMQPublisher publisher;

    public Page<SalesPerson> getSalespersonsPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        logger.info("Fetching all Salespersons sorted by: " + sortBy);
        return salespersonrepo.findAll(pageable);
    }


    public List<SalesPerson> getallSalesPerson(){
        logger.info("Fetching all salespersons");
        return salespersonrepo.findAll();
    }
@Cacheable(value = "salespersons", key = "#id")
    public SalesPerson getSalesPersonbyID(int id){
        logger.info("Fetching salesperson by id: {}", id);
        return salespersonrepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Salesperson not found with id: {}", id);
                    return new ResourceNotFoundException("Salesperson with id " + id + " not found");
                });
    }

    @CachePut(value="salespersons", key = "#resultid")
    public SalesPerson createnewSalesPerson(SalesPerson salesPerson){
        logger.info("Creating new salesperson: {}", salesPerson.getName());
        salespersonrepo.save(salesPerson);
        SalesPersonEvent event = new SalesPersonEvent();
        event.setEventType("SALESPERSON_CREATED");
        event.setId(salesPerson.getId());
        event.setName(salesPerson.getName());
        event.setEmail(salesPerson.getEmail());
        publisher.sendSalespersonEvent(event);
        return salesPerson;
    }

    @CacheEvict(value = "salespersons", key = "id")
    public void deletesalesPersonbyId(int id){
        SalesPerson salesPerson = salespersonrepo.findById(id).orElseThrow(() -> new RuntimeException("Salesperson not found with ID: " + id));
        logger.info("Deleting salesperson with id: {}", id);
        SalesPersonEvent event = new SalesPersonEvent();
        event.setEventType("SALESPERSON_DELETED");
        event.setId(salesPerson.getId());
        event.setName(salesPerson.getName());
        event.setEmail(salesPerson.getEmail());
        publisher.sendSalespersonEvent(event);
        salespersonrepo.deleteById(id);
        logger.info("Deleted salesperson with id: {}", id);
    }

    @CachePut(value = "salespersons", key = "#result.id")
    public SalesPerson updateSalesPinfo(int id, SalesPerson salesperson){
        logger.info("Updating salesperson with id: {}", id);
        SalesPerson oldsalespersoninfo = salespersonrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salesperson with id " + id + " not found"));
        oldsalespersoninfo.setName(salesperson.getName());
        oldsalespersoninfo.setEmail(salesperson.getEmail());
        oldsalespersoninfo.setSales(salesperson.getSales());
        SalesPersonEvent event = new SalesPersonEvent();
        event.setEventType("SALESPERSON_CREATED");
        event.setId(oldsalespersoninfo.getId());
        event.setName(oldsalespersoninfo.getName());
        event.setEmail(oldsalespersoninfo.getEmail());
        publisher.sendSalespersonEvent(event);
        return salespersonrepo.save(oldsalespersoninfo);
    }
}
