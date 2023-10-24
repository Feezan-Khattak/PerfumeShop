package com.bs.application.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "template")
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(columnDefinition="TEXT", nullable = false)
    private String template;

    @Column(nullable = false)
    private String subject;

    @Basic
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @PrePersist
    protected  void onCreate(){
        if(createdAt == null){
            createdAt = new Timestamp(System.currentTimeMillis());
        }
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = new Timestamp(System.currentTimeMillis());
    }
    @Column(nullable = false, unique = true)
    private String templateKey;
}
