package com.project.aws.controllers;

import com.project.aws.services.ImageTransformServices;
import com.project.aws.services.S3Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(maxAge = 3600, origins = "*")
@RestController
public class S3Controller {

    @Autowired
    S3Services s3Services;
    @Autowired
    ImageTransformServices imageTransformServices;

    @GetMapping("/")
    public ResponseEntity<?> getUploadMainPage() {
        return new ResponseEntity<>(s3Services.getUploadMainPage(), HttpStatus.OK);
    }

    @GetMapping("/d/{filename}")
    public ResponseEntity<?> download(@PathVariable String filename) {
        return new ResponseEntity<>(s3Services.download(filename), HttpStatus.OK);
    }

    @GetMapping("/aws-url/{filename}")
    public ResponseEntity<?> getAwsS3Url(@PathVariable String filename) throws Exception {
        return new ResponseEntity<>(s3Services.getAwsS3Url(filename), HttpStatus.OK);
    }
}
