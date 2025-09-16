package com.yeshwanth.ps.repository;

import com.yeshwanth.ps.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product,String> {
}
