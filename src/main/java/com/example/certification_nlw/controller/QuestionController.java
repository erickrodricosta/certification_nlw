package com.example.certification_nlw.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.example.certification_nlw.dto.AlternativesResultDTO;
import com.example.certification_nlw.dto.QuestionResultDTO;
import com.example.certification_nlw.model.Alternatives;
import com.example.certification_nlw.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.certification_nlw.repository.QuestionRepository;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/technology/{technology}")
    public List<QuestionResultDTO> findByTechnology(@PathVariable String technology) {
        System.out.println("TECH === " + technology);
        var result = this.questionRepository.findByTechnology(technology);

        var toMap = result.stream().map(question -> mapQuestionToDTO(question))
                .collect(Collectors.toList());
        return toMap;
    }

    static QuestionResultDTO mapQuestionToDTO(Question question) {
        var questionResultDTO = QuestionResultDTO.builder()
                .id(question.getId())
                .technology(question.getTechnology())
                .description(question.getDescription()).build();

        List<AlternativesResultDTO> alternativesResultDTOs = question.getAlternatives()
                .stream().map(alternative -> mapAlternativeDTO(alternative))
                .collect(Collectors.toList());

        questionResultDTO.setAlternatives(alternativesResultDTOs);
        return questionResultDTO;
    }

    static AlternativesResultDTO mapAlternativeDTO(Alternatives alternativesResultDTO) {
        return AlternativesResultDTO.builder()
                .id(alternativesResultDTO.getId())
                .description(alternativesResultDTO.getDescription()).build();
    }
}
