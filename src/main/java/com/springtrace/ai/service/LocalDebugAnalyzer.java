package com.springtrace.ai.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

/** Lightweight deterministic checks. These are hints, not a Java compiler/static analyzer. */
@Service
public class LocalDebugAnalyzer {
    public String analyze(String language, String issueType, String content) {
        String lower = content.toLowerCase();
        List<String> findings = new ArrayList<>();

        if (lower.contains(".touppercase()") || lower.contains(".tolowercase()")
                || lower.contains(".length()") || lower.contains(".trim()")) {
            findings.add("**Possible null dereference:** a method is called directly on an object. If that reference is null, Java throws `NullPointerException`. Validate it or use an explicit null-safe design.");
        }
        if (lower.contains("new bankaccount(null") || lower.contains("= null;")) {
            findings.add("**Null value found:** the input explicitly assigns or passes `null`. Trace every later use of that variable and validate it at the boundary.");
        }
        if (lower.contains("balance = balance - amount") || lower.contains("balance -= amount")) {
            findings.add("**Possible negative-balance bug:** subtraction alone does not prevent withdrawing more than the available balance. Check `amount > 0` and `amount <= balance` before updating the balance.");
        }
        if (lower.contains("withdraw(") && !lower.contains("insufficient balance") && !lower.contains("amount > balance")) {
            findings.add("**Missing withdrawal guard (possible):** verify that the method rejects amounts greater than the current balance and rejects zero or negative amounts.");
        }
        if (lower.contains("nullpointerexception")) {
            findings.add("**NullPointerException in trace:** inspect the exact application stack-trace line immediately below the exception and identify which reference is null.");
        }
        if (lower.contains("404") || lower.contains("not found")) {
            findings.add("**Possible HTTP 404:** verify the request URL, HTTP method, controller mapping, path variables and whether the requested resource exists.");
        }
        if (lower.contains("no qualifying bean") || lower.contains("nosuchbeandefinition") || lower.contains("not a managed type")) {
            findings.add("**Possible Spring/JPA configuration issue:** check component/entity scanning, annotations, package structure and repository generic types.");
        }
        if (lower.contains("sqlsyntaxerrorexception") || lower.contains("syntax error") || lower.contains("bad sql grammar")) {
            findings.add("**Possible SQL syntax problem:** check table/column names, reserved keywords, parameter binding and the SQL dialect configured for the database.");
        }
        if (findings.isEmpty()) {
            Matcher exception = Pattern.compile("(?i)([A-Za-z0-9_.]+Exception|Error)").matcher(content);
            if (exception.find()) {
                findings.add("**Exception detected:** `" + exception.group(1) + "`. Start with the first `Caused by` entry and the first stack-trace line belonging to your own package.");
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("## Local static checks (no API key required)\n\n")
              .append("**Language:** ").append(safe(language)).append(" · **Category:** ").append(safe(issueType)).append("\n\n")
              .append("### Findings\n");
        if (findings.isEmpty()) {
            report.append("No supported pattern was confidently detected by the local checks. This does not prove the code is correct. Add the full exception/stack trace or connect BharatCode for deeper reasoning.\n");
        } else {
            for (int i = 0; i < findings.size(); i++) {
                report.append(i + 1).append(". ").append(findings.get(i)).append("\n\n");
            }
        }
        report.append("### Next steps\n")
              .append("- Run the code or the failing test to confirm the actual behavior.\n")
              .append("- Add a regression test for each confirmed issue.\n")
              .append("- Treat these pattern checks as suggestions; they do not replace compilation, tests or a full static-analysis tool.\n");
        return report.toString();
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "Not specified" : value;
    }
}
