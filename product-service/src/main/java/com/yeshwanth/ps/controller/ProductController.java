package com.yeshwanth.ps.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeshwanth.ps.dto.ProductRequest;
import com.yeshwanth.ps.dto.ProductResponse;
import com.yeshwanth.ps.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(
            @RequestPart("product") String product, 
            @RequestPart MultipartFile file )
    {
        ObjectMapper om = new ObjectMapper();
        ProductRequest req = null;
        
        try {
            req = om.readValue(product, ProductRequest.class);
        } catch (JsonProcessingException ex) {
            return new ResponseEntity<>("Invalid JSON format", HttpStatus.BAD_REQUEST);
        }
        
        ProductResponse productResponse = productService.addProduct(req, file);
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }
}
