package com.example.ragserver.controller;

import com.example.ragserver.model.FinancialRagService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


/**
 * Controller class for handling HTTP requests related to document upload and chat functionality.
 * This class provides endpoints for uploading PDF documents and loading the chat interface.
 * It integrates with the {@link FinancialRagService} to process uploaded documents and support
 * Retrieval-Augmented Generation (RAG) based queries.
 *
 * <p>Key endpoints:</p>
 * <ul>
 *     <li>{@code POST /upload}: Accepts a PDF file upload, processes it using the {@link FinancialRagService},
 *     and redirects to the chat page.</li>
 *     <li>{@code GET /chat}: Renders the chat interface for interacting with the RAG-based system.</li>
 * </ul>
 *
 * <p>Example workflow:</p>
 * <ol>
 *     <li>User uploads a PDF document via the {@code /upload} endpoint.</li>
 *     <li>The document is processed and stored by the {@link FinancialRagService}.</li>
 *     <li>User is redirected to the {@code /chat} page to interact with the system.</li>
 * </ol>
 *
 * <p>Dependencies:</p>
 * <ul>
 *     <li>{@link FinancialRagService}: Handles document processing and RAG-based querying.</li>
 * </ul>
 *
 * @see FinancialRagService
 * @see MultipartFile
 * @see Controller
 */

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