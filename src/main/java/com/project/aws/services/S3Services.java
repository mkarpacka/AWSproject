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
import org.springframework.web.multipart.MultipartFile;

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

    public boolean uploadFile(MultipartFile file) {

        File f = new File(file.getOriginalFilename());
        System.out.println(f.getAbsolutePath());
        String path = f.getAbsolutePath();

        try {
            s3.putObject(bucketName, file.getOriginalFilename(), moveAndStoreFile(file, path));
            return true;
        } catch (SdkClientException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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

    //

    public void fukc123() throws IOException {
        String bucketName = "hellobucket";
        String objectKey = "facebook.jpg"; //PRZEKAZAC NAZWE

        try {
            // Set the presigned URL to expire after one hour.
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 60;
            expiration.setTime(expTimeMillis);

            // Generate the presigned URL.
            System.out.println("Generating pre-signed URL.");
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, objectKey)
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(expiration);
            URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);

            System.out.println("Pre-Signed URL: " + url.toString());
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }


//    ///
//    public static void myAttempt() throws Exception {
//
//        String policy_document = constructPolicy();
//        String aws_secret_key="123_sercet_key";
//
//        String policy = Base64.getEncoder().encode(
//                policy_document.getBytes("UTF-8")).toString().replaceAll("\n","").replaceAll("\r","");
//
//        String dateStamp ="20200912";
//        String region = "us-east-1";
//        String serviceName ="s3";
//        System.out.println("NEW SIGNATURE: "+getSignature(getSignatureKey(aws_secret_key,dateStamp,region,serviceName)));
//
//        System.out.println("ENCODED POLICY: "+policy);
//    }
//
//    private static String constructPolicy() throws UnsupportedEncodingException {
//
//        String policy_document="{\"expiration\": \"2021-01-01T00:00:00Z\",\n" +
//                "  \"conditions\": [ \n" +
//                "    {\"bucket\": \"hellobucket\"}, \n" +
//                "    [\"starts-with\", \"$key\", \"uploads/\"],\n" +
//                "    {\"acl\": \"private\"},\n" +
//                "    {\"success_action_redirect\": \"http://localhost/\"},\n" +
//                "    [\"starts-with\", \"$Content-Type\", \"\"],\n" +
//                "    [\"content-length-range\", 0, 1048576]\n" +
//                "  ]\n" +
//                "}";
//
//        String policy = Base64.getEncoder().encode(
//                policy_document.getBytes("UTF-8")).toString().replaceAll("\n","").replaceAll("\r","");
//        return policy;
//    }
//
//    private static byte[] HmacSHA256(String data, byte[] key) throws Exception {
//        String algorithm="HmacSHA256";
//        Mac mac = Mac.getInstance(algorithm);
//        mac.init(new SecretKeySpec(key, algorithm));
//        return mac.doFinal(data.getBytes("UTF8"));
//    }
//
//    private static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception  {
//        byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
//        byte[] kDate    = HmacSHA256(dateStamp, kSecret);
//        byte[] kRegion  = HmacSHA256(regionName, kDate);
//        byte[] kService = HmacSHA256(serviceName, kRegion);
//        byte[] kSigning = HmacSHA256("aws4_request", kService);
//        return kSigning;
//    }
//
//    private static String getSignature(byte[] key) throws Exception{
//
//        return BaseEncoding.base16().lowerCase().encode(HmacSHA256(constructPolicy(), key));
//    }
}
