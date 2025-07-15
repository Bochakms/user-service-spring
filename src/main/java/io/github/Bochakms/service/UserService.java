package io.github.Bochakms.service;


import io.github.Bochakms.dto.UserEvent;
import io.github.Bochakms.dto.UserEventType;
import io.github.Bochakms.dto.UserRequest;
import io.github.Bochakms.dto.UserResponse;
import io.github.Bochakms.entity.User;
import io.github.Bochakms.exeption.UserNotFoundException;

import io.github.Bochakms.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public UserResponse createUser(UserRequest userRequest) {
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword()); // In production, password should be encoded
        
        User savedUser = userRepository.save(user);
                
     // Отправка события в Kafka
        kafkaTemplate.send("user-events", new UserEvent(UserEventType.CREATED, savedUser.getEmail()));
        
        return mapToUserResponse(savedUser);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return mapToUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        
        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    public void deleteUser(Long id) {
       
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        
        String email = user.getEmail();
        userRepository.deleteById(id);
        
     // Отправка события в Kafka
        kafkaTemplate.send("user-events", new UserEvent(UserEventType.DELETED, email));
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}