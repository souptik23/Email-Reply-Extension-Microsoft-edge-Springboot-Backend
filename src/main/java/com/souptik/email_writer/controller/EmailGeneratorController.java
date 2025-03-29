package com.souptik.email_writer.controller;

import com.souptik.email_writer.model.EmailRequest;
import com.souptik.email_writer.service.EmailGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/mail")
public class EmailGeneratorController {

    @Autowired
    private EmailGeneratorService emailGeneratorService;
    
    @CrossOrigin(origins = "*")
    @PostMapping("/generate")
    public ResponseEntity<String> generateMail(@RequestBody EmailRequest emailRequest){
        String response = emailGeneratorService.generateEmailReply(emailRequest);
        return ResponseEntity.ok(response);
    }
}
