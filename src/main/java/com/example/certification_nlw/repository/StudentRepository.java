package com.example.certification_nlw.repository;

import com.example.certification_nlw.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    public Optional<Student> findByEmail(String email);
}
