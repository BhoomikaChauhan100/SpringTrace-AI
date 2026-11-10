package com.springtrace.ai.service;

import com.springtrace.ai.dto.AnalysisRequest;
import com.springtrace.ai.dto.AnalysisResponse;
import com.springtrace.ai.entity.AnalysisHistory;
import com.springtrace.ai.repository.AnalysisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AiAnalysisService {
    private final BharatCodeService ai;
    private final AnalysisRepository repository;

    public AiAnalysisService(BharatCodeService ai, AnalysisRepository repository) {
        this.ai = ai;
        this.repository = repository;
    }

    @Transactional
    public AnalysisResponse analyze(AnalysisRequest request) {
        String result = ai.analyze(request.getLanguage(), request.getIssueType(), request.getContent());
        String mode = ai.mode();
        AnalysisHistory history = new AnalysisHistory();
        history.setInput(request.getContent());
        history.setResult(result);
        history.setLanguage(request.getLanguage());
        history.setIssueType(request.getIssueType());
        history.setMode(mode);
        repository.save(history);
        return new AnalysisResponse(result, mode);
    }
}
