package com.yeshwanth.ps.service;

import com.yeshwanth.ps.dto.ProductRequest;
import com.yeshwanth.ps.dto.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    String uploadFile(MultipartFile file);

    Boolean deleteFile(String fileName);

    ProductResponse addProduct(ProductRequest productRequest,MultipartFile file);

   ProductResponse getProductById(String id);

   List<ProductResponse> getProducts();

    void deleteProductById(String id);

}
