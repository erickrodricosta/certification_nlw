package com.example.certification_nlw.service;

import java.util.List;

import com.example.certification_nlw.model.Certification;
import com.example.certification_nlw.repository.CertificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Top10RankingUseCase {

    @Autowired
    private CertificationRepository certificationStudentRepository;

    public List<Certification> execute() {
        var result = this.certificationStudentRepository.findTop10ByOrderByGradeDesc();
        return result;
    }
}
