package com.bs.application.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "address")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address extends BaseEntity{

    @Basic
    @Column(name = "country")
    private String country;

    @Basic
    @Column(name = "zip_code")
    private String zipCode;

    @Basic
    @Column(name = "address_1")
    private String address1;

    @Basic
    @Column(name = "address_2")
    private String address2;

    @OneToOne(mappedBy = "address")
    private User user;
}
