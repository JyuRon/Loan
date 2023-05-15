package com.example.repository;

import com.example.domain.Judgement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JudgementRepository extends JpaRepository<Judgement, Long> {

    Optional<Judgement> findByApplicationId(Long applicationId);
}
