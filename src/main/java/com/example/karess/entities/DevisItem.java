package com.example.karess.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DevisItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000) // Pour les descriptions longues
    private String designation;

    private String unite;     // U, m², Pce, etc.
    private Double quantite;
    private Double prixUnitaire;
    private Double montantHt;
}