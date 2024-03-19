package com.example.certification_nlw.service;

import com.example.certification_nlw.dto.StudentCertificationAnswerDTO;
import com.example.certification_nlw.dto.VerifyHasCertificationDTO;
import com.example.certification_nlw.model.AnswersCertification;
import com.example.certification_nlw.model.Certification;
import com.example.certification_nlw.model.Question;
import com.example.certification_nlw.model.Student;
import com.example.certification_nlw.repository.CertificationRepository;
import com.example.certification_nlw.repository.QuestionRepository;
import com.example.certification_nlw.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StudentCertificationAnswersUseCase {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CertificationRepository certificationStudentRepository;

    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    public Certification execute(StudentCertificationAnswerDTO dto) throws Exception {

        var hasCertification = this.verifyIfHasCertificationUseCase
                .execute(new VerifyHasCertificationDTO(dto.getEmail(), dto.getTechnology()));

        if (hasCertification) {
            throw new Exception("Você já tirou sua certificação!");
        }

        // Buscar as alternativas das perguntas
        // - Correct ou Incorreta
        List<Question> questionsEntity = questionRepository.findByTechnology(dto.getTechnology());
        List<AnswersCertification> answersCertifications = new ArrayList<>();

        AtomicInteger correctAnswers = new AtomicInteger(0);

        dto.getQuestionsAnswers()
                .stream().forEach(questionAnswer -> {
                    var question = questionsEntity.stream()
                            .filter(q -> q.getId().equals(questionAnswer.getQuestionID())).findFirst().get();

                    var findCorrectAlternative = question.getAlternatives().stream()
                            .filter(alternative -> alternative.isCorrect()).findFirst().get();

                    if (findCorrectAlternative.getId().equals(questionAnswer.getAlternativeID())) {
                        questionAnswer.setCorrect(true);
                        correctAnswers.incrementAndGet();
                    } else {
                        questionAnswer.setCorrect(false);
                    }

                    var answerrsCertificationsEntity = AnswersCertification.builder()
                            .answerID(questionAnswer.getAlternativeID())
                            .questionID(questionAnswer.getQuestionID())
                            .isCorrect(questionAnswer.isCorrect()).build();

                    answersCertifications.add(answerrsCertificationsEntity);
                });

        // Verificar se existe student pelo email
        var student = studentRepository.findByEmail(dto.getEmail());
        UUID studentID;
        if (student.isEmpty()) {
            var studentCreated = Student.builder().email(dto.getEmail()).build();
            studentCreated = studentRepository.save(studentCreated);
            studentID = studentCreated.getId();
        } else {
            studentID = student.get().getId();
        }

        Certification certificationStudentEntity = Certification.builder()
                .technology(dto.getTechnology())
                .studentID(studentID)
                .grade(correctAnswers.get())
                .build();

        var certificationStudentCreated = certificationStudentRepository.save(certificationStudentEntity);

        answersCertifications.stream().forEach(answerCertification -> {
            answerCertification.setCertificationID(certificationStudentEntity.getId());
            answerCertification.setCertification(certificationStudentEntity);
        });

        certificationStudentEntity.setAnswersCertification(answersCertifications);

        certificationStudentRepository.save(certificationStudentEntity);

        return certificationStudentCreated;
        // Salvar as informações da certificação
    }
}