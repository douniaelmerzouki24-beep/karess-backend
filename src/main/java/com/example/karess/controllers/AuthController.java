package com.example.karess.controllers;

import com.example.karess.entities.Utilisateur;
import com.example.karess.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // باش يخلي Angular يتواصل معاه بلا مشاكل CORS
public class AuthController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Utilisateur user) {
        return utilisateurRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword())
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(u)) // حددنا النوع هنا صراحة لمنع الاختلاط
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants incorrects"));
    }
}
