package com.bs.application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "reset_password")
@EqualsAndHashCode(callSuper = true)
public class ResetPassword extends BaseEntity {
    @Column(name = "token")
    private String token;

    @Column(name = "expire_at")
    private Timestamp expiresAt;

    @Column(name = "email_sent")
    private boolean emailSent;

    @Basic
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user_id;
}
