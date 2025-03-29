package com.souptik.email_writer.model;

import lombok.Data;

@Data
public class EmailRequest {

    private String emailContent;
    private String tone;  // professional(default) , //sarcastic etc
}
