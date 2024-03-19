package com.example.certification_nlw.repository;

import java.util.List;
import java.util.UUID;

import com.example.certification_nlw.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestionRepository extends JpaRepository<Question, UUID> {

    List<Question> findByTechnology(String technology);
}