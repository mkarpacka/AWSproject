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

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (s3Services.uploadFile(file)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/save-edited-photo/{filename}")
    public ResponseEntity<?> saveEditedPhoto(@PathVariable String filename) throws IOException {
        s3Services.uploadFile(imageTransformServices.saveGraphicAsImage(filename));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/policy")
    public ResponseEntity<?> funkcja() throws Exception {
        s3Services.fukc123();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
