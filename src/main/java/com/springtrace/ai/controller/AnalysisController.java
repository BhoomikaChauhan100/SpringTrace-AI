package com.springtrace.ai.controller;
import com.springtrace.ai.dto.*;
import com.springtrace.ai.repository.AnalysisRepository;
import com.springtrace.ai.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class AnalysisController {
 private final AiAnalysisService service; private final BharatCodeService ai; private final AnalysisRepository repo;
 public AnalysisController(AiAnalysisService service,BharatCodeService ai,AnalysisRepository repo){this.service=service;this.ai=ai;this.repo=repo;}
 @PostMapping("/analyze") public String analyze(@Valid @ModelAttribute AnalysisRequest request,BindingResult errors,Model model){
  if(errors.hasErrors()){model.addAttribute("liveMode",ai.isConfigured());model.addAttribute("validationError",errors.getAllErrors().get(0).getDefaultMessage());return "debugger";}
  try {
   AnalysisResponse response=service.analyze(request); model.addAttribute("analysis",response.analysis());
   String rendered = HtmlRenderer.builder().build().render(Parser.builder().build().parse(response.analysis()));
   model.addAttribute("analysisHtml", Jsoup.clean(rendered, Safelist.relaxed().addTags("pre", "code").addAttributes("code", "class")));
   model.addAttribute("mode",response.mode());
   model.addAttribute("originalInput",request.getContent());model.addAttribute("language",request.getLanguage());model.addAttribute("issueType",request.getIssueType());return "result";
  } catch(Exception ex){model.addAttribute("liveMode",ai.isConfigured());model.addAttribute("errorMessage",ex.getMessage());return "debugger";}
 }
 @PostMapping("/history/{id}/delete") public String delete(@PathVariable Long id,RedirectAttributes flash){
  repo.deleteById(id);flash.addFlashAttribute("message","Analysis deleted.");return "redirect:/history";
 }
}
