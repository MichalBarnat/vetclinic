package com.powtorka.vetclinic.model.security.permission;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

@Entity
@Table(name = "permissions")
@Data
@Generated
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EPermission name;
}
