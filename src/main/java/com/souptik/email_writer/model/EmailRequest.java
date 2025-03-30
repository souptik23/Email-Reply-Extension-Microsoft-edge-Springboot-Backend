package com.souptik.email_writer.model;

import lombok.Data;

@Data
public class EmailRequest {

    private String emailContent;
    private String tone;  // professional(default) , //sarcastic etc
    private String relationshipContext;  // e.g., "colleague", "client", "friend"
    private String responseGoal;         // e.g., "confirm receipt", "schedule meeting", "provide information"
    private String preferredLength;      // e.g., "short", "medium", "detailed"
}
