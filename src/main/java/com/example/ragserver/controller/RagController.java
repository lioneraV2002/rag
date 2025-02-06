package com.example.ragserver.controller;

import com.example.ragserver.model.FinancialRagService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class RagController {

	private final FinancialRagService documentService;
	
	
	public RagController(FinancialRagService documentService) {
		this.documentService = documentService;
	}

	@PostMapping("/upload")
	public String uploadDocument(@RequestParam("fileToUpload") MultipartFile document) {
		documentService.uploadPDF(document.getResource());
		
		return "redirect:/chat";
	}
	
	@GetMapping("/chat")
	public String loadChat() {
		return "chat";
	}
}