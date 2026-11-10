package com.springtrace.ai.controller;
import com.springtrace.ai.repository.AnalysisRepository;
import com.springtrace.ai.service.BharatCodeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {
 private final AnalysisRepository repo; private final BharatCodeService ai;
 public HomeController(AnalysisRepository repo,BharatCodeService ai){this.repo=repo;this.ai=ai;}
 @GetMapping("/") public String dashboard(Model model){
  model.addAttribute("recent",repo.findTop20ByOrderByCreatedAtDesc()); model.addAttribute("total",repo.count());
  model.addAttribute("liveMode",ai.isConfigured()); return "index";
 }
 @GetMapping("/debugger") public String debugger(Model model){model.addAttribute("liveMode",ai.isConfigured());return "debugger";}
 @GetMapping("/history") public String history(Model model){model.addAttribute("history",repo.findTop20ByOrderByCreatedAtDesc());return "history";}
}
