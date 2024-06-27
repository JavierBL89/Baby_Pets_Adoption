
package com.example.bb_pets_adoption.pet_listing.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/***
 * Service class to upload files into Amazon S3 Cloud Services
 * **/
@Service
public class S3Service {

	// vars
    private final AmazonS3 s3Client;

    
    
    @Value("${aws.s3.bucket}")
    private String bucketName;

    // constructor
    public S3Service(@Value("${aws.access.key}") String accessKey,
                     @Value("${aws.secret.key}") String secretKey,
                     @Value("${aws.region}") String region) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    /***
     * Upload file into a S3 bucket
     * 
     * @param file - the byte file to convert to a string
     **/
    public String uploadFile(MultipartFile file) {
        File convertedFile = convertMultiPartToFile(file);
        String fileUrl = "https://" + bucketName + ".s3.eu-west-1.amazonaws.com/" + file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, file.getOriginalFilename(), convertedFile));
        convertedFile.delete();
        return fileUrl;
    }

    
    /***
     * 
     * @param  file - the muiltypart file to be converted into a standard file object
     **/
    private File convertMultiPartToFile(MultipartFile file) {
    	
    	// create a file instance
        File convFile = new File(file.getOriginalFilename());
        
        // try with resources
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convFile;
    }
}
