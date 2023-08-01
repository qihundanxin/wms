package com.deer.wms.file.configurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3WmsClientConfig {

    private String accessKeyId = "AKIAQIMGJCV4R4QTX77N";
    private String secretKeyId = "uCI5FzcyBhPKX7wKM53JFgwZTfy23bPLVhP1/WcP";
    private Regions clientRegion = Regions.CA_CENTRAL_1;

    @Bean
    public AmazonS3 S3client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretKeyId);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(clientRegion)
                .build();
        return s3Client;
    }
}