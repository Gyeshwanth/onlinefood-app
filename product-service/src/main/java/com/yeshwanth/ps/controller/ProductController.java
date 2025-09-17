package com.yeshwanth.ps.controller;

import com.yeshwanth.ps.dto.ProductRequest;
import com.yeshwanth.ps.dto.ProductResponse;
import com.yeshwanth.ps.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@Valid @RequestPart("product") ProductRequest product, @RequestPart  MultipartFile file) {
        ProductResponse productResponse = productService.addProduct(product, file);
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }
}
