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
@CrossOrigin(origins = "*")
public class FactureController {

    @Autowired
    private FactureService factureService;

    @Autowired
    private FactureRepository factureRepository;

    // هاد الميتود دابا راها داخل الكلاس بشكل صحيح ومحمية
    private ResponseEntity<byte[]> createPdfResponse(byte[] pdf, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", filename);
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

} // هاد القوس هو نهاية الكلاس ومجموع فيه كلشي دابا!