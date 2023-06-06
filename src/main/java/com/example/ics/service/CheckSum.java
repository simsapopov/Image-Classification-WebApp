package com.example.ics.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
@RequiredArgsConstructor
public class CheckSum {
    public String getChecksum(String input) {
        if (input.contains("http")) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<byte[]> response = restTemplate.getForEntity(input, byte[].class);
                byte[] imageBytes = response.getBody();
                if (imageBytes != null) {
                    String checksum = DigestUtils.sha256Hex(imageBytes);
                    return checksum;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        try {
            byte[] imageBytes = Base64.decodeBase64(input);
            String checksum = DigestUtils.sha256Hex(imageBytes);
            return checksum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

