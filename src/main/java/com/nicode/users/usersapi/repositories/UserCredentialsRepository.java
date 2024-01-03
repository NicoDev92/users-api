package com.nicode.users.usersapi.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.nicode.users.usersapi.models.entities.UserCredentials;

public interface UserCredentialsRepository extends CrudRepository<UserCredentials, Long>{
    Optional<UserCredentials> findByUsernameLike(String email);
    Optional<UserCredentials> findByUserInfoUserIdLike(Long userId);
}
