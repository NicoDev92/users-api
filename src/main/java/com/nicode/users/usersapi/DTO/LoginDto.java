package com.nicode.users.usersapi.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginDto {
    private String username;
    private String password;
}
