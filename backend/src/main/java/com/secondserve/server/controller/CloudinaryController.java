package com.secondserve.server.controller;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cloudinary")
public class CloudinaryController {

    @Autowired
    private Cloudinary cloudinary;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;
    @GetMapping("/signature")
    public ResponseEntity<Map<String, Object>> getUploadSignature() {
        long timestamp = System.currentTimeMillis() / 1000;

        Map<String, Object> paramsToSign = new HashMap<>();
        paramsToSign.put("timestamp", timestamp);
        paramsToSign.put("folder", "secondserve/food-items");

        String signature = cloudinary.apiSignRequest(paramsToSign, cloudinary.config.apiSecret,cloudinary.config.signatureVersion);

        Map<String, Object> response = new HashMap<>();
        response.put("signature", signature);
        response.put("timestamp", timestamp);
        response.put("apiKey", apiKey);
        response.put("cloudName", cloudName);
        response.put("folder", "secondserve/food-items");

        return ResponseEntity.ok(response);
    }
}