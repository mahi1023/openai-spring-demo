package com.example.demo.text.prompttemplate;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.services.OpenAiService;
import com.example.demo.text.prompttemplate.records.Interview;

@Controller
public class InterviewHelperController {
	@Autowired
	private OpenAiService service;

	@GetMapping("/showInterviewHelper")
	public String showInterviewHelper() {
		return "interviewHelper";
	}

	@PostMapping("/interviewHelper")
	public String interviewHelper(@RequestParam("company") String company,
			@RequestParam("jobTitle") String jobTitle,
			@RequestParam("strength") String strength, 
			@RequestParam("weakness") String weakness, Model model) {
	ChatResponse c =	service.genrateInterviewQuestion(company,jobTitle,strength,weakness);
	String response = c.getResult().getOutput().getText();
	
	model.addAttribute("response",response);
		return "interviewHelper";
	}

}
