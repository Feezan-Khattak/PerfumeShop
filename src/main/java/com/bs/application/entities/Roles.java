package com.bs.application.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Roles extends BaseEntity{
    @Basic
    @Column(name = "role")
    private String role;

    @Basic
    @Column(name = "description")
    private String description;

    @OneToOne(mappedBy = "roles")
    private User user;
}
