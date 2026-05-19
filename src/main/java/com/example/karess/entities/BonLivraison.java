package com.example.karess.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BonLivraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroBL;      // N° :
    private String clientNom;     // Destinataire :
    private String bcNumero;      // N° BC :
    private String objet;         // Objet :
    private LocalDate dateLivraison; // El Aïoun le 14/04/2026

    // --- CHAMPS DE CALCUL MANQUANTS ---
    private Double totalHt;       // Pour le champ "Total HTVA"

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String cachetBase64;  // Pour l'image dans le cadre "Client"

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bl_id")
    private List<BLItem> items = new ArrayList<>();

    // --- HELPERS ---
    @PrePersist
    protected void onCreate() {
        if (this.dateLivraison == null) {
            this.dateLivraison = LocalDate.now(); // Met la date du jour par défaut
        }
    }
}