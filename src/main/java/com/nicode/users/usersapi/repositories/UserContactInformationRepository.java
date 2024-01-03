package com.nicode.users.usersapi.repositories;

import org.springframework.data.repository.CrudRepository;

import com.nicode.users.usersapi.models.entities.UserContactInformation;

public interface UserContactInformationRepository extends CrudRepository<UserContactInformation, Long>{
    
}
