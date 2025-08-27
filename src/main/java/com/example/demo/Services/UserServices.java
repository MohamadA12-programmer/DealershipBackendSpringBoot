package com.example.demo.Services;

import com.example.demo.POJO.SalesPersonEvent;
import com.example.demo.POJO.UserEvent;
import com.example.demo.entity.Car;
import com.example.demo.entity.User;
import com.example.demo.Exception.ResourceNotFoundException;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.messaging.RabbitMQPublisher;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {

    private static final Logger logger = LoggerFactory.getLogger(UserServices.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RabbitMQPublisher publisher;


    public Page<User> getUserPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        logger.info("Fetching all Users sorted by: " + sortBy);
        return userRepository.findAll(pageable);
    }


    public List<User> getAllUsers(){
        logger.info("Fetching all users");
        return userRepository.findAll();
    }

    @Cacheable(value = "users", key = "#id")
    public User getUserById(int id){
        logger.info("Fetching user by id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found with id: {}", id);
                    return new ResourceNotFoundException("User with id " + id + " not found");
                });
    }
    @CachePut(value = "users", key = "#result.id")
    public User createUser(User user){

        logger.info("Creating user: {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        UserEvent event = new UserEvent();
        event.setEventType("USER_CREATED");
        event.setId(user.getId());
        event.setRole(user.getRole());
        event.setUsername(user.getUsername());
        publisher.sendUserEvent(event);
        return user;
    }

    @CachePut(value = "users", key = "#result.id")
    public User updateUser(int id, User updatedUser){
        logger.info("Updating user with id: {}", id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        existingUser.setRole(updatedUser.getRole());

        UserEvent event = new UserEvent();
        event.setEventType("USER_UPDATED");
        event.setId(existingUser.getId());
        event.setUsername(existingUser.getUsername());
        event.setRole(existingUser.getRole());
        publisher.sendUserEvent(event);

        return userRepository.save(existingUser);
    }

    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(int id){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        logger.info("Deleting user with id: {}", id);
        UserEvent event = new UserEvent();
        event.setEventType("USER_DELETED");
        event.setId(user.getId());
        event.setUsername(user.getUsername());
        event.setRole(user.getRole());
        publisher.sendUserEvent(event);
        userRepository.deleteById(id);
        logger.info("Deleted user with id: {}", id);
    }
}
