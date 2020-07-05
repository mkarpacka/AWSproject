package com.project.aws.controllers;

import com.project.aws.services.S3Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(maxAge = 3600, origins = "*")
@RestController
public class S3Controller {

    @Autowired
    S3Services s3Services;

    @GetMapping("/")
    public ResponseEntity<?> getUploadMainPage() {
        return new ResponseEntity<>(s3Services.getUploadMainPage(), HttpStatus.OK);
    }

    @GetMapping("/d/{filename}")
    public ResponseEntity<?> download(@PathVariable String filename) {
        return new ResponseEntity<>(s3Services.download(filename), HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (s3Services.uploadFile(file)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
}
