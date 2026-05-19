package com.example.karess.controllers;

import com.example.karess.entities.Facture;
import com.example.karess.entities.FactureItem;
import com.example.karess.services.FactureService;
import com.example.karess.repositories.FactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/factures")
@CrossOrigin("*")
public class FactureController {

    @Autowired
    private FactureService factureService;

    @Autowired
    private FactureRepository factureRepository;

    @PostMapping("/save")
    public Facture saveFacture(@RequestBody Facture facture) {
        if (facture.getItems() != null) {
            facture.getItems().forEach(item -> {
                item.setMontantHt(item.getQuantite() * item.getPrixUnitaire());
                item.setFacture(facture);
            });
        }

        double totalHT = facture.getItems().stream()
                .mapToDouble(FactureItem::getMontantHt)
                .sum();

        facture.setTotalHt(totalHT);
        double taux = (facture.getTvaRate() != null) ? facture.getTvaRate() : 20.0;
        double montantTVA = totalHT * (taux / 100.0);
        facture.setTotalTva(montantTVA);
        facture.setTotalTtc(totalHT + montantTVA);

        return factureRepository.save(facture);
    }

    @GetMapping("/generate-pdf/{id}")
    public ResponseEntity<byte[]> generateFacture(@PathVariable Long id) {
        try {
            byte[] pdf = factureService.generateFacturePdf(id);
            return createPdfResponse(pdf, "Facture.pdf");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<byte[]> createPdfResponse(byte[] pdf, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", filename);
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}