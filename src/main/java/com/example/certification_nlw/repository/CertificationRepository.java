package com.example.certification_nlw.repository;

import com.example.certification_nlw.model.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, UUID> {

    @Query("SELECT c FROM certifications c INNER JOIN c.studentEntity std WHERE std.email = :email AND c.technology = :technology")
    List<Certification> findByStudentEmailAndTechnology(String email, String technology);

    @Query("SELECT c from certifications c ORDER BY c.grade DESC LIMIT 10")
    List<Certification> findTop10ByOrderByGradeDesc();

}
