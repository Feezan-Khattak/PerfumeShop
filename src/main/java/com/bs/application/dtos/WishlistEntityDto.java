package com.bs.application.dtos;

import com.bs.application.entities.Perfume;
import com.bs.application.entities.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class WishlistEntityDto extends BaseEntityDto{
    private Perfume perfume;
    private User user;
}
