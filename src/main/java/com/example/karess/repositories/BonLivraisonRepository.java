package com.example.karess.repositories;

import com.example.karess.entities.BonLivraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonLivraisonRepository extends JpaRepository<BonLivraison, Long> {
}