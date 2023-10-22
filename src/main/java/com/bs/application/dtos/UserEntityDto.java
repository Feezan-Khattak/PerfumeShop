package com.bs.application.dtos;

import com.bs.application.security.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class UserEntityDto extends BaseEntityDto {
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String gender;
    private String image;
    private String userId;
    private AddressEntityDto address;
    private CompanyEntityDto company;
    private Role role;
}
