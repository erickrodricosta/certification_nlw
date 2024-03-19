package com.example.certification_nlw.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "certifications")
@Builder

public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100)
    private String technology;

    @Column(length = 10)
    private int grade;

    @Column(name = "student_id")
    private UUID studentID;

    @ManyToOne
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student studentEntity;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "answer_certification_id", insertable = false, updatable = false)
    @JsonManagedReference
    List<AnswersCertification> answersCertification;
}
