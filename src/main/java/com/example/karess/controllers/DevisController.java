package com.example.karess.controllers;

import com.example.karess.entities.Devis;
import com.example.karess.services.DevisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;
@RestController
@RequestMapping("/api/devis")
@CrossOrigin("*") // Pour permettre les appels depuis ton Frontend (React/Angular)
public class DevisController {

    @Autowired
    private DevisService devisService;

    // 1. Enregistrer un nouveau devis
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Devis> createDevis(
            @RequestPart("devis") String devisJson,
            @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        Devis devis = objectMapper.readValue(devisJson, Devis.class);

        if (file != null && !file.isEmpty()) {
            // Transformation du fichier en texte Base64
            String base64 = Base64.getEncoder().encodeToString(file.getBytes());
            devis.setCachetBase64(base64);
        }

        return ResponseEntity.ok(devisService.saveDevis(devis));
    }

    // 2. Générer le PDF du Devis
    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> generatePDF(@PathVariable Long id) {
        byte[] pdfContents = devisService.generateDevisPdf(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // "inline" permet d'ouvrir le PDF dans le navigateur au lieu de le télécharger directement
        headers.setContentDispositionFormData("inline", "Devis_" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContents);
    }
}