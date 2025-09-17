package com.yeshwanth.ps.service.impl;

import com.yeshwanth.ps.dto.ProductRequest;
import com.yeshwanth.ps.dto.ProductResponse;
import com.yeshwanth.ps.dto.mapper.ProductMapper;
import com.yeshwanth.ps.entity.Product;
import com.yeshwanth.ps.exception.FileUploadException;
import com.yeshwanth.ps.exception.InvalidFileException;
import com.yeshwanth.ps.repository.ProductRepository;
import com.yeshwanth.ps.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

 //   private final S3Client s3Client;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Value("${server.port}")
    private String serverPort;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${aws.s3.region}")
    private String awsRegion;

    // File validation constants
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/jpg", "image/png"
    );
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png"
    );

    // @Override
    // public String uploadFile(MultipartFile file) {
    //     log.info("Starting S3 file upload for file: {}", file.getOriginalFilename());

    //     // Validate file
    //     validateFile(file);

    //     String originalFilename = file.getOriginalFilename();
    //     if (originalFilename == null || originalFilename.trim().isEmpty()) {
    //         throw new InvalidFileException("Invalid filename");
    //     }

    //     String fileExtension = getFileExtension(originalFilename);
    //     String key = UUID.randomUUID().toString() + "." + fileExtension;

    //     try {
    //         PutObjectRequest putObjectRequest = PutObjectRequest.builder()
    //                 .bucket(bucketName)
    //                 .key(key)
    //                 .contentType(file.getContentType())
    //                 .contentLength(file.getSize())
    //                 .build();

    //         PutObjectResponse response = s3Client.putObject(putObjectRequest,
    //                 RequestBody.fromBytes(file.getBytes()));

    //         if (response.sdkHttpResponse().isSuccessful()) {
    //             String s3Url = String.format("https://%s.s3.%s.amazonaws.com/%s",
    //                     bucketName, awsRegion, key);
    //             log.info("Successfully uploaded file to S3: {}", s3Url);
    //             return s3Url;
    //         }
    //     } catch (IOException ex) {
    //         log.error("S3 file upload failed", ex);
    //         throw new FileUploadException("S3 file upload failed: " + ex.getMessage());
    //     }
    //     throw new FileUploadException("An error occurred while uploading the file to S3");
    // }

    @Override
    public String uploadFileLocal(MultipartFile file) {
        log.info("Starting local file upload for file: {}", file.getOriginalFilename());

        // Validate file
        validateFile(file);

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new InvalidFileException("Invalid filename");
        }

        String extension = getFileExtension(originalFilename);
        String fileName = UUID.randomUUID().toString() + "." + extension;

        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("Created upload directory: {}", uploadPath.toAbsolutePath());
            }

            // Save file outside JAR
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            // Build proper URL with port
            String fileUrl = String.format("http://localhost:%s/images/%s", serverPort, fileName);
            log.info("Successfully uploaded file locally: {}", fileUrl);
            return fileUrl;

        } catch (IOException e) {
            log.error("Local file upload failed", e);
            throw new FileUploadException("Local file upload failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ProductResponse addProduct(ProductRequest productRequest, MultipartFile file) {
        log.info("Adding new product: {}", productRequest.getName());


        Product product = productMapper.dtoToEntity(productRequest);

        // Upload image if provided
        if (file != null && !file.isEmpty()) {
            String imageUrl = uploadFileLocal(file); // Use local for development
            product.setImageUrl(imageUrl);
        }

        Product savedProduct = productRepository.save(product);
        log.info("Successfully added product with id: {}", savedProduct.getId());
        return productMapper.entityToDto(savedProduct);
    }

    // Private validation methods
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("File is required and cannot be empty");
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidFileException("File too large. Maximum size allowed is " + (MAX_FILE_SIZE / (1024 * 1024)) + "MB");
        }

        // Validate content type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new InvalidFileException("Invalid file type. Allowed types: " + ALLOWED_CONTENT_TYPES);
        }

        // Validate file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && !hasValidExtension(originalFilename)) {
            throw new InvalidFileException("Invalid file extension. Allowed extensions: " + ALLOWED_EXTENSIONS);
        }
    }


    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            throw new InvalidFileException("File must have a valid extension");
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    private boolean hasValidExtension(String filename) {
        try {
            String extension = getFileExtension(filename);
            return ALLOWED_EXTENSIONS.contains(extension);
        } catch (Exception e) {
            return false;
        }
    }
}
