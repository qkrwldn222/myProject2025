package com.reservation.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles" , schema = "common")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;
}
