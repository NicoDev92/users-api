package com.nicode.users.usersapi.services;

import java.util.List;
import java.util.Optional;

import com.nicode.users.usersapi.DTO.UserDto;
import com.nicode.users.usersapi.models.entities.User;

public interface UserService {
    List<User> findAll();

    Optional<User> findById(Long id);

    User create(UserDto userDto);

    User update(Long id, User userUpdates);

    void delete(Long id);

} 