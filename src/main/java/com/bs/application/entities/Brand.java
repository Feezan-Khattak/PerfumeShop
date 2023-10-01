package com.bs.application.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "brand")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Brand extends BaseEntity {
    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "brand")
    private List<Perfume> perfumes = new ArrayList<>();
}
