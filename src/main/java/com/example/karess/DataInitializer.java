package com.example.karess;

import com.example.karess.entities.*;
import com.example.karess.repositories.FactureRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final FactureRepository factureRepository;

    public DataInitializer(FactureRepository factureRepository) {
        this.factureRepository = factureRepository;
    }

    @Override
    public void run(String... args) {
        // On crée une facture de test seulement si la base est vide
        if (factureRepository.count() == 0) {
            Facture f = new Facture();
            f.setNumeroFacture("FAC-2024-001");
            f.setDateFacture("29/04/2024");
            // ... (haut du fichier)
            f.setClientNom("LafargeHolcim Maroc");
            f.setClientAdresse("6, route de Mekka, Casablanca");
            f.setClientIce("123456789"); // Nouveau champ
            f.setBcNumero("BC-2024-99"); // Nouveau champ
            f.setObjetFacture("Travaux de fondation"); // Nouveau champ

// Remplace f.setTva(20.0) par :
            f.setTvaRate(20.0);
            f.setTotalTva(5000.0); // (20% de 25000)

            f.setTotalHt(25000.0);
            f.setTotalTtc(30000.0);
// ...
            factureRepository.save(f);
            System.out.println(">> Facture de test insérée avec succès !");
        }
    }
}