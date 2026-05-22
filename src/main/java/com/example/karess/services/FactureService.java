package com.example.karess.services;

import com.example.karess.entities.Facture;
import com.example.karess.repositories.FactureRepository;
import com.example.karess.utils.NombreOutils; // N'oublie pas d'importer ta classe Cent
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class FactureService {

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private PdfService pdfService;
    private String formatNumberWithSpaces(String number) {
        if (number == null) return "";
        // Hadi kat-zid espace bin kol 2 d l'arqaam
        return number.replaceAll("(.{2})", "$1 ").trim();
    }
    private String formaterMontant(Double montant) {
        if (montant == null) return "0,00";
        // DecimalFormat avec le symbole de groupage (espace ou point)
        java.text.DecimalFormatSymbols symbols = new java.text.DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Point pour les milliers
        symbols.setDecimalSeparator(',');  // Virgule pour les décimales

        java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00", symbols);
        return df.format(montant);
    }
    public byte[] generateFacturePdf(Long id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture introuvable"));

        Double total = facture.getTotalTtc();
        if (total == null) total = 0.0;

        // Utilisation de la nouvelle classe qui gère les milliers
        long dirhams = total.longValue();
        int centimes = (int) Math.round((total - dirhams) * 100);

        String phrase = NombreOutils.convertir(dirhams) + " Dirhams";

        if (centimes > 0) {
            phrase += " et " + NombreOutils.convertir(centimes) + " Centimes";
        }
        phrase += " TTC";

        Map<String, Object> data = new HashMap<>();
        data.put("facture", facture);
        data.put("montantEnLettres", phrase );
// Ghadi n-jbdou l'BC N° mn l'objet (facture ou bl)
        String bcRaw = facture.getBcNumero(); // Awla getNumBc() 3la hsab chnou smit-ha 3ndk
        String bcFormate = formatNumberWithSpaces(bcRaw);
        data.put("totalHtFormate", formaterMontant(facture.getTotalHt()));
        data.put("tvaFormate", formaterMontant(facture.getTotalTva()));
        data.put("totalTtcFormate", formaterMontant(facture.getTotalTtc()));
        data.put("bcNumeroFormate", bcFormate);
        return pdfService.generatePdfFromHtml("test_karess", data);

    }
    public Facture saveFacture(Facture facture) {
        return factureRepository.save(facture);
    }
}