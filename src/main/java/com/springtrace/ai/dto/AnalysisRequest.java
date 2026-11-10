package com.springtrace.ai.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class AnalysisRequest {
 @NotBlank(message="Please paste an error, code snippet or stack trace.")
 @Size(max=20000, message="Input must be 20,000 characters or fewer.")
 private String content;
 @jakarta.validation.constraints.NotBlank(message="Please select a programming language.")
 @jakarta.validation.constraints.Size(max=40)
 private String language="Java";
 @jakarta.validation.constraints.NotBlank(message="Please select an issue category.")
 @jakarta.validation.constraints.Size(max=60)
 private String issueType="AUTO";
 public String getContent(){return content;} public void setContent(String v){content=v;}
 public String getLanguage(){return language;} public void setLanguage(String v){language=v;}
 public String getIssueType(){return issueType;} public void setIssueType(String v){issueType=v;}
}
