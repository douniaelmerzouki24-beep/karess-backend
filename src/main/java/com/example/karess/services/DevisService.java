package com.example.karess.services;

import com.example.karess.entities.Devis;
import com.example.karess.entities.DevisItem;
import com.example.karess.entities.DevisSection;
import com.example.karess.repositories.DevisRepository;
import com.example.karess.utils.NombreOutils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

@Service
public class DevisService {

    @Autowired
    private DevisRepository devisRepository;

    @Autowired
    private PdfService pdfService;

    public Devis saveDevis(Devis devis) {
        double totalGeneral = 0;

        for (DevisSection section : devis.getSections()) {
            for (DevisItem item : section.getItems()) {
                // Calcul du montant HT pour chaque ligne
                if (item.getQuantite() != null && item.getPrixUnitaire() != null) {
                    item.setMontantHt(item.getQuantite() * item.getPrixUnitaire());
                    totalGeneral += item.getMontantHt();
                }
            }
        }
        devis.setTotalGeneralHt(totalGeneral);
        return devisRepository.save(devis);
    }
    public byte[] generateDevisPdf(Long id) {
        Devis devis = devisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Devis introuvable"));
        if (devis.getCachetBase64() == null || devis.getCachetBase64().isEmpty()) {
            System.out.println("ATTENTION : Le devis " + id + " n'a pas de cachet en base de données !");
        } else {
            System.out.println("SUCCÈS : Cachet trouvé pour le devis " + id);
        }
        Double total = devis.getTotalGeneralHt();

        // Utilisation de la nouvelle classe qui gère les milliers
        long dirhams = total.longValue();
        int centimes = (int) Math.round((total - dirhams) * 100);

        String phrase = NombreOutils.convertir(dirhams) + " Dirhams";

        if (centimes > 0) {
            phrase += " et " + NombreOutils.convertir(centimes) + " Centimes";
        }

        phrase += " Hors Taxes";
        Map<String, Object> data = new HashMap<>();
        data.put("devis", devis);
        // 2. On crée la variable pour le HTML
        data.put("phraseTotale", phrase);

        return pdfService.generatePdfFromHtml("devis-template", data);
    }
}