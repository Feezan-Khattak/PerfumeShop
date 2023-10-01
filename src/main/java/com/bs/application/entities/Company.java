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
@Table(name = "company")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Company extends BaseEntity{
    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "website")
    private String website;

    @Basic
    @Column(name = "fax_number")
    private String faxNumber;

    @OneToOne(mappedBy = "company")
    private User user;
}
