package com.bs.application.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class AddressEntityDto extends BaseEntityDto {
    private String country;
    private String zipCode;
    private String address1;
    private String address2;
}
