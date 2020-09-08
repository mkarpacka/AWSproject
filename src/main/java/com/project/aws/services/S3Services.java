package com.project.aws.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.project.aws.entities.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;



@Service
public class S3Services {
    @Autowired
    private AmazonS3 s3;

    @Value("${s3.bucket}")
    private String bucketName;

    public ArrayList<Image> getUploadMainPage() {
        ListObjectsV2Result result = s3.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        ArrayList<Image> images = new ArrayList<>();
        int i = 0;
        for (S3ObjectSummary os : objects) {
            images.add(new Image(i++, os.getKey()));
        }

        return images;
    }


    public String download(String nameOfFileToDownload) {

        System.out.format("Downloading %s from S3 bucket %s...\n", nameOfFileToDownload, bucketName);

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

        return nameOfFileToDownload;
    }

    public String getAwsS3Url(String filename) throws IOException {
        try {
            // Set the presigned URL to expire after one hour.
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 60;
            expiration.setTime(expTimeMillis);

            // Generate the presigned URL.
            System.out.println("Generating pre-signed URL.");
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, filename)
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(expiration);
            URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);

            System.out.println("Pre-Signed URL: " + url.toString());

            return url.toString();
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }

        return filename;
    }
}
