package com.example.demo.text;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.services.OpenAiService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AnswerAnyThingController {

	@Autowired
    private OpenAiService chatService;

    @GetMapping("/showAskAnything")
    public String showAskAnything() {
         return "askAnything";
    }

//    @PostMapping("/askAnything")
//    public String askAnything(@RequestParam("question") String question, Model model) {
//
//    	ChatResponse response = chatService.generateAnswer(question);
//    	System.out.print(response);
//    	model.addAttribute("question",question);
//    	model.addAttribute("answer",response.getResult().getOutput().getText());
//
//        return "askAnything";
//    }
    @PostMapping("/askAnything")
    public String askAnything(@RequestParam("question") String question, 
            HttpSession session, 
            Model model) {

// Get or create conversationId from session
String conversationId = (String) session.getAttribute("conversationId");
if (conversationId == null) {
conversationId = "user-" + session.getId();
session.setAttribute("conversationId", conversationId);
}

ChatResponse response = chatService.generateAnswer(question, conversationId);
System.out.print(response);
model.addAttribute("question", question);
model.addAttribute("answer", response.getResult().getOutput().getText());

return "askAnything";
}
}