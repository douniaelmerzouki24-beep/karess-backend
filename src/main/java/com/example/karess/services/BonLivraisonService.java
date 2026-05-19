package com.example.karess.services;

import com.example.karess.entities.BonLivraison;
import com.example.karess.repositories.BonLivraisonRepository;
import com.example.karess.utils.NombreOutils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class BonLivraisonService {

    @Autowired
    private BonLivraisonRepository blRepository;

    @Autowired
    private PdfService pdfService;
    private String formatNumberWithSpaces(String number) {
        if (number == null) return "";
        // Hadi kat-zid espace bin kol 2 d l'arqaam
        return number.replaceAll("(.{2})", "$1 ").trim();
    }

    public BonLivraison saveBonLivraison(BonLivraison bl) {
        return blRepository.save(bl);
    }

    // Cette méthode manquait et causait l'erreur rouge dans le Controller
    public byte[] generateBlPdf(Long id) {
        BonLivraison bl = blRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BL introuvable"));

        Double total = bl.getTotalHt();
        if (total == null) total = 0.0;

        // Utilisation de la nouvelle classe qui gère les milliers
        long dirhams = total.longValue();
        int centimes = (int) Math.round((total - dirhams) * 100);

        String phrase = NombreOutils.convertir(dirhams) + " Dirhams";

        if (centimes > 0) {
            phrase += " et " + NombreOutils.convertir(centimes) + " Centimes";
        }
        phrase += " HTVA";

        // 3. Préparation des données pour le template
        Map<String, Object> data = new HashMap<>();
        String bcRaw = bl.getBcNumero(); // Awla getNumBc() 3la hsab chnou smit-ha 3ndk
        String bcFormate = formatNumberWithSpaces(bcRaw);
        data.put("bl", bl); // ou "bonLivraison" selon ton template
        data.put("phraseTotale", phrase); // C'est cette variable qu'on affiche
        data.put("bcNumeroFormate", bcFormate);
        return pdfService.generatePdfFromHtml("bl-template", data);
    }
}