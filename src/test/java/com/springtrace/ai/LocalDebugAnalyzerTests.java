package com.springtrace.ai;

import com.springtrace.ai.service.LocalDebugAnalyzer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LocalDebugAnalyzerTests {
    private final LocalDebugAnalyzer analyzer = new LocalDebugAnalyzer();

    @Test
    void detectsUnsafeStringMethodCallAndNullInput() {
        String code = "String name = null; System.out.println(name.toUpperCase());";
        String report = analyzer.analyze("Java", "AUTO", code);
        assertThat(report).contains("Possible null dereference", "Null value found");
    }

    @Test
    void detectsPossibleNegativeBankBalance() {
        String code = "void withdraw(double amount) { balance = balance - amount; }";
        String report = analyzer.analyze("Java", "AUTO", code);
        assertThat(report).contains("negative-balance", "withdrawal guard");
    }

    @Test
    void doesNotPretendUnknownCodeWasDiagnosed() {
        String report = analyzer.analyze("Java", "AUTO", "class Hello { void run() {} }");
        assertThat(report).contains("No supported pattern was confidently detected");
    }
}
