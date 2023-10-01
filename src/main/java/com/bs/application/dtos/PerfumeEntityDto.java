package com.bs.application.dtos;

import com.bs.application.entities.Brand;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class PerfumeEntityDto extends BaseEntityDto{
    private String name;
    private String description;
    private String upc;
    private String itemNumber;
    private String gender;
    private Double price;
    private String uuid;
    private String image;
    private String quantity;
    private String countryOfOrigin;
    private BrandEntityDto brand;
}
