/**
 * 
 */
package com.example.bb_pets_adoption.pet_listing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.bb_pets_adoption.pet_listing.service.S3Service;


/**
 * Controller class to receive files in a POST request, and delegate upload file operation to S3Service class
 */
@RestController
public class FileController {

    @Autowired
    private S3Service s3Service;

    /**
     * Endpoint to handle uploading files into Amazon S3 Services
     * **/
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = s3Service.uploadFile(file);
        return ResponseEntity.ok(fileUrl);
    }
}
