package com.nicode.users.usersapi.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users_contact_info")
@Getter
@Setter
public class UserContactInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userContactInfoId;
    
    private String phoneNumber;
    
    private String email;
    
    private String address;
}
