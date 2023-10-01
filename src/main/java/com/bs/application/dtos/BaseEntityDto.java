package com.bs.application.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class BaseEntityDto {
    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
