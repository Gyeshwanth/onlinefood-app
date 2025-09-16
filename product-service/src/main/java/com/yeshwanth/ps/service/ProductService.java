package com.yeshwanth.ps.service;

import com.yeshwanth.ps.dto.ProductRequest;
import com.yeshwanth.ps.dto.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    String uploadFile(MultipartFile file);

    ProductResponse addProduct(ProductRequest productRequest,MultipartFile file);


    //for production i use this s3 bucket for images for local save images folder under resources/static/images and given url r path in imageurl and save db
     String uploadFileLocal(MultipartFile file);

}
