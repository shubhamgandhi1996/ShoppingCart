package com.shubhamgandhi.ProductService.service;

import com.shubhamgandhi.ProductService.model.ProductRequest;
import com.shubhamgandhi.ProductService.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
