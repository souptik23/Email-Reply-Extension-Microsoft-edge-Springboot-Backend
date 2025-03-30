package com.souptik.email_writer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.souptik.email_writer.model.EmailRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Data
@Service
public class EmailGeneratorService  {
    //calling the api

    private final WebClient webClient;

    @Value("${gemini.api.url}")  // getting the values from the properties file
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public EmailGeneratorService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String generateEmailReply(EmailRequest emailRequest){
        // build a prompt
        String prompt = buildPrompt(emailRequest);

        // generate a request from that prompt
        Map<String , Object> request = Map.of(
                "contents" , new Object[] {
                        Map.of("parts" , new Object[]{
                                Map.of( "text" , prompt)
                        })
                }
        );

        // fire the request and get the response
        // we will use the webclient , this is build upon project rector and it enables us to asynchronous http request
        String response = webClient.post()
                .uri(geminiApiUrl)
                .header("Content-Type" , "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class )
                .block();

        // Extract the response and return
        return extractResponseContent(response);

    }

    private String extractResponseContent(String response) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);  // will get the json tree

            String emailReply = root.path("candidates")      // Navigate JSON tree
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            // Trim leading/trailing spaces and normalize multiple newlines
            return emailReply;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return "error processing response";
        }
    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("generate a reply fot this mail content . please dont generate a subject line");
        if(emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()){
            prompt.append("use a ").append(emailRequest.getTone()).append(" tone.");
        }
        else{
            prompt.append("use a ").append("professional").append(" tone.");
        }
        prompt.append("\nOriginal email content :").append(emailRequest.getEmailContent());
        return prompt.toString();
    }
}
