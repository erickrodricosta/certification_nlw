package com.example.certification_nlw.controller;

import java.util.List;

import com.example.certification_nlw.model.Certification;
import com.example.certification_nlw.service.Top10RankingUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ranking")
public class RankingController {

    @Autowired
    private Top10RankingUseCase top10RankingUseCase;

    @GetMapping("/top10")
    public List<Certification> top10() {
        return this.top10RankingUseCase.execute();
    }
}
