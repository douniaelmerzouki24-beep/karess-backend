package com.example.karess.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class BLItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String designation;
    private String unite;
    private Double quantite;
    private Double prixUnitaire;
    private Double montantHt;
}