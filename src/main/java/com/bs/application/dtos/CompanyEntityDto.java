package com.bs.application.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class CompanyEntityDto extends UserEntityDto {
    private String name;
    private String website;
    private String faxNumber;
}
