package com.project.aws.controllers;

import com.project.aws.services.S3Services;
import com.project.aws.services.SqsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(maxAge = 3600, origins = "*")
@RestController
public class SqsController {

    @Autowired
    SqsServices sqsServices;

    @GetMapping("/get-message")
    public ResponseEntity<?> getMessage() {
        return new ResponseEntity<>(sqsServices.getMessageToQueue(), HttpStatus.OK);
    }

    @GetMapping("/send-message")
    public ResponseEntity<?> sendMessage() {
        return new ResponseEntity<>(sqsServices.sendMessageFromQueue(), HttpStatus.OK);
    }
}
