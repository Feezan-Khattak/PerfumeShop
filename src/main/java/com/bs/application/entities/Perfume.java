package com.bs.application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "perfume")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Perfume extends BaseEntity {
    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "upc")
    private String upc;

    @Basic
    @Column(name = "item_number")
    private String itemNumber;

    @Basic
    @Column(name = "gender")
    private String gender;

    @Basic
    @Column(name = "price")
    private Double price;

    @Basic
    @Column(name = "uuid")
    private String uuid;

    @Basic
    @Column(name = "image")
    private String image;

    @Basic
    @Column(name = "quantity")
    private String quantity;

    @Basic
    @Column(name = "country_of_origin")
    private String countryOfOrigin;

    @Basic
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "brand")
    private Brand brand;

    @OneToOne(mappedBy = "perfume")
    private Wishlist wishlist;

}
