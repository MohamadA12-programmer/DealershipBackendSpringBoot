package com.example.demo.Services;

import com.example.demo.POJO.CarEvent;
import com.example.demo.entity.Car;
import com.example.demo.Exception.ResourceNotFoundException;
import com.example.demo.Repositories.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.demo.messaging.RabbitMQPublisher;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CarServices {

    private static final Logger logger = LoggerFactory.getLogger(CarServices.class);

    @Autowired
    private CarRepository carrepo;

    @Autowired
    private RabbitMQPublisher publisher;


    public Page<Car> getCarsPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        logger.info("Fetching all Cars sorted by: " + sortBy);
        return carrepo.findAll(pageable);
    }

    @Cacheable(value = "cars")
    public List<Car> getallcars(){
        logger.info("Fetching all cars");
        return carrepo.findAll();
    }
  @Cacheable(value = "cars", key = "#id")
    public Car getCarbyId(int id){
        logger.info("Fetching car by id: {}", id);
        System.out.println("Fetching fro db: Car with ID = " + id);
        return carrepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Car not found with id: {}", id);
                    return new ResourceNotFoundException("Car with id " + id + " not found");
                });
    }
    @CachePut(value="cars", key = "#result.id")
    public Car createCar(Car car) {
        logger.info("Creating new car:  {} {}", car.getMake(), car.getModel());
        Car savedCar = carrepo.save(car);

        CarEvent event = new CarEvent();
        event.setEventType("CAR_CREATED");
        event.setCarId(savedCar.getId());
        event.setMake(savedCar.getMake());
        event.setModel(savedCar.getModel());

        publisher.sendCarEvent(event);
        return savedCar;
    }

    @CacheEvict(value = "cars", key = "#id")
    public void deleteCar(int id){
        logger.info("Deleting car with id: {}", id);
        Car car = carrepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
        CarEvent event = new CarEvent();
        event.setEventType("CAR_DELETED");
        event.setCarId(id);
        event.setMake(car.getMake());
        event.setModel(car.getModel());
        publisher.sendCarEvent(event);
        carrepo.deleteById(id);
        logger.info("Deleted car with id: {}", id);
    }

    @CachePut(value = "cars", key = "#result.id")
    public Car updateCar(int id, Car newdetails){
        logger.info("Updating car with id: {}", id);
        Car oldcar = carrepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Car with id " + id + " not found"));
        oldcar.setModel(newdetails.getModel());
        oldcar.setMake(newdetails.getMake());
        oldcar.setYear(newdetails.getYear());
        oldcar.setPrice(newdetails.getPrice());
        oldcar.setSales(newdetails.getSales());

        CarEvent event= new CarEvent();
        event.setEventType("CAR_UPDATED");
        event.setCarId(oldcar.getId());
        event.setMake(oldcar.getMake());
        event.setModel(oldcar.getModel());
        publisher.sendCarEvent(event);
        return carrepo.save(oldcar);
    }
}
