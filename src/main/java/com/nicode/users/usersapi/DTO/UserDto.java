package com.nicode.users.usersapi.DTO;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDto {

    private Long userId;

    private String firstName;
    @NotEmpty
    private String lastName;
    private String idNumber;
    private Date dateOfBirth;

    private Long userContactInfoId;
    private String phoneNumber;
    @Email
    @Size(min = 4, max = 75)
    @NotEmpty
    private String email;
    private String address;

    private Long userCredentialId;
    private String username;
    private String password;
    private Boolean disabled;
    private Boolean locked;
    private String role;
}
