package com.example.karess.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
public class FactureItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String designation;
    private String unite;
    private double quantite;
    private double prixUnitaire;
    private double montantHt;
    private String quantiteSaisie; // Contiendra "1" ou "20%"

    @ManyToOne
    @JoinColumn(name = "facture_id")
    @JsonIgnore
    private Facture facture;

    // 1. Méthode pour lier la facture (Corrigée)
    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    // 2. Méthode de calcul (Placée EN DEHORS de setFacture)
    public Double getMontantHt() {
        if (this.quantiteSaisie == null || this.quantiteSaisie.isEmpty()) {
            return 0.0;
        }

        try {
            String val = this.quantiteSaisie.trim();

            // Si l'utilisateur a saisi un pourcentage (ex: 20%)
            if (val.endsWith("%")) {
                double valeurNumerique = Double.parseDouble(val.replace("%", "").replace(",", "."));
                double pourcentage = valeurNumerique / 100.0;
                return this.prixUnitaire * pourcentage;
            } else {
                // Si l'utilisateur a saisi un chiffre normal (ex: 1)
                double qte = Double.parseDouble(val.replace(",", "."));
                return this.prixUnitaire * qte;
            }
        } catch (Exception e) {
            // Retourne 0 si la saisie n'est pas un nombre valide
            return 0.0;
        }
    }
}