package com.springtrace.ai.repository;
import com.springtrace.ai.entity.AnalysisHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AnalysisRepository extends JpaRepository<AnalysisHistory,Long> {
 List<AnalysisHistory> findTop20ByOrderByCreatedAtDesc();
}
