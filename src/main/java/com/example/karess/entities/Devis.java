package com.example.karess.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Devis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroOffre; // ex: 023/2026
    private String clientNom;   // ex: LafargeHolcim Maroc

    @Column(length = 1000)
    private String objet;      // L'objet détaillé de l'offre
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String cachetBase64; // Pour stocker le tampon
    private LocalDate dateDevis;
    private Double totalGeneralHt;

    // Relation avec les sections (6-1, 6-2, etc.)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "devis_id")
    private List<DevisSection> sections = new ArrayList<>();
}