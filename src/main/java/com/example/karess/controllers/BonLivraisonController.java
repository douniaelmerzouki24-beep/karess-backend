package com.example.karess.controllers;

import com.example.karess.entities.BonLivraison;
import com.example.karess.services.BonLivraisonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;

@RestController
@RequestMapping("/api/bl")
@CrossOrigin("*") // Autorise Angular (port 60756) à communiquer avec Java
public class BonLivraisonController {

    @Autowired
    private BonLivraisonService blService;

    @Autowired
    private ObjectMapper objectMapper; // Injecté via le Bean dans KaressApplication

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveBL(
            @RequestPart("bl") String blJson,
            @RequestPart(value = "cachetFile", required = false) MultipartFile cachetFile) {
        try {
            // 1. Conversion du JSON en Objet Java
            // Utilise le JavaTimeModule configuré pour éviter l'erreur 500 sur les dates
            BonLivraison bl = objectMapper.readValue(blJson, BonLivraison.class);

            // 2. Traitement de l'image du cachet
            if (cachetFile != null && !cachetFile.isEmpty()) {
                String base64Cachet = Base64.getEncoder().encodeToString(cachetFile.getBytes());
                bl.setCachetBase64(base64Cachet);
            }

            // 3. Sauvegarde en base de données
            BonLivraison savedBl = blService.saveBonLivraison(bl);

            // On retourne l'objet sauvegardé (qui contient maintenant son ID)
            return ResponseEntity.ok(savedBl);

        } catch (Exception e) {
            // Affiche l'erreur précise dans la console IntelliJ pour le débogage
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) {
        try {
            // Appelle la méthode du service (doit être identique au nom dans BonLivraisonService)
            byte[] pdf = blService.generateBlPdf(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            // "inline" permet d'ouvrir le PDF directement dans le navigateur
            headers.setContentDisposition(ContentDisposition.inline().filename("Bon_Livraison_" + id + ".pdf").build());

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Si l'ID n'existe pas (Erreur BL introuvable), on renvoie 404 au lieu de 500
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}