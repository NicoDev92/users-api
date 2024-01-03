package com.nicode.users.usersapi.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicode.users.usersapi.DTO.UserDto;
import com.nicode.users.usersapi.exceptions.EntityAlreadyExistsException;
import com.nicode.users.usersapi.models.entities.User;
import com.nicode.users.usersapi.services.UserServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/usersApi")
@CrossOrigin("*")
@Validated
public class UserController {

    @Autowired
    private UserServiceImpl service;

    @GetMapping("/users")
    public List<User> findAll() {
        return this.service.findAll();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<User> user = this.service.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveUser(@Valid @RequestBody UserDto userDto, 
        BindingResult result) throws EntityAlreadyExistsException {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(validation(result));
        }
        try {
            String successMessage = this.service.create(userDto);
            Map<String, String> response = new HashMap<>();
            response.put("message", successMessage);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EntityAlreadyExistsException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> update(
            @PathVariable Long id,
            @Valid @RequestBody UserDto user,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(validation(result));
        }

        try {
            String succesMessage = this.service.update(id, user);
            Map<String, String> response = new HashMap<>();
            response.put("message", succesMessage);

            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        try {
            String succesMessage = this.service.delete(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", succesMessage);

            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    private Map<String, String> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(
                    err.getField(), "Field " + err.getField() + ": " + err.getDefaultMessage());
        });
        return errors;
    }
}