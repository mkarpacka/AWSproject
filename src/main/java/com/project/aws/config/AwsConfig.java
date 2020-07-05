package com.project.aws.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {
    @Value("${aws_access_key_id}")
    private String awsId;

    @Value("${aws_secret_access_key}")
    private String awsKey;

    @Value("${aws_session_token}")
    private String sessionToken;

    @Value("${s3.region}")
    private String region;

    @Bean
    public AmazonS3 s3client() {
        BasicSessionCredentials awsCreds = new BasicSessionCredentials(awsId, awsKey, sessionToken);
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    @Bean
    public AmazonSQS sqsClient() {
        BasicSessionCredentials awsCreds = new BasicSessionCredentials(awsId, awsKey, sessionToken);
        return AmazonSQSClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }
}
