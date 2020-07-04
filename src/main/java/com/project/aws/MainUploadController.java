package com.project.aws;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@CrossOrigin(maxAge = 3600, origins = "*")
@RestController
public class MainUploadController {

    private String bucketName = "hellobucket";

    @GetMapping("/")
    public ResponseEntity<?> getUploadMainPage() {

        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();

        ListObjectsV2Result result = s3.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        ArrayList<Image> images = new ArrayList<>();
        int i = 0;
        for (S3ObjectSummary os : objects) {
            images.add(new Image(i++, os.getKey()));
        }

        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @GetMapping("/d/{filename}")
    public ResponseEntity<?> download(@PathVariable String filename) {

        String nameOfFileToDownload = filename;
        System.out.format("Downloading %s from S3 bucket %s...\n", nameOfFileToDownload, bucketName);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();
        try {
            S3Object o = s3.getObject(bucketName, nameOfFileToDownload);
            S3ObjectInputStream s3is = o.getObjectContent();
            FileOutputStream fos = new FileOutputStream(new File(nameOfFileToDownload));
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            s3is.close();
            fos.close();
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return new ResponseEntity<>(nameOfFileToDownload, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {

        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();

        File f = new File(file.getOriginalFilename());
        System.out.println(f.getAbsolutePath());
        String path = f.getAbsolutePath();

        try {
            s3.putObject(bucketName, file.getOriginalFilename(), moveAndStoreFile(file, path));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SdkClientException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    public static File moveAndStoreFile(MultipartFile file, String path) throws IOException {
        File fileToSave = new File(path);
        fileToSave.createNewFile();
        FileOutputStream fos = new FileOutputStream(fileToSave);
        fos.write(file.getBytes());
        fos.close();
        return fileToSave;
    }
}
