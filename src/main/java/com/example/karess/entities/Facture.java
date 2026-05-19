package com.example.karess.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Facture {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Champs existants ---
    private String numeroFacture;
    private String dateFacture;
    private String clientNom;

    // --- NOUVEAUX CHAMPS À AJOUTER ---
    private String clientAdresse; // Adresse précise sous le nom
    private String clientIce;     // L'ICE du client
    private String bcNumero;      // Numéro de Bon de Commande
    private String objetFacture;  // L'objet de la prestation
    private String quantiteSaisie;
    // --- Lignes de facture ---
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "facture_id")
    private List<FactureItem> items;

    // --- Totaux calculés ---
    private Double totalHt;
    private Double tvaRate = 20.0; // Taux par défaut
    private Double totalTva;       // Le montant de la TVA
    private Double totalTtc;
    private String totalTtcLibelle;
}