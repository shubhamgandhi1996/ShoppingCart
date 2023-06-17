package com.shubhamgandhi.ProductService.service;

import com.shubhamgandhi.ProductService.Exception.ProductServiceCustomException;
import com.shubhamgandhi.ProductService.entity.Product;
import com.shubhamgandhi.ProductService.model.ProductRequest;
import com.shubhamgandhi.ProductService.model.ProductResponse;
import com.shubhamgandhi.ProductService.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2 //due to lombok no need to create the logger object just use this annotaion the object will be available
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding Product...");

        //this is builder pattern automatically this getters and setters got created due to the lombok
        Product product= Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product); // to save into the database
        log.info("Product created..");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("getting a product with id: "+ productId);
        Product product=
                productRepository.findById(productId)
                        .orElseThrow(() ->new ProductServiceCustomException("product with given id not found.", "PRODUCT_NOT_FOUND"));
        //now we have created the custom exception but we need to handle the exceptino also -> we will need a custom exception
        //we need to send

        ProductResponse productResponse= new ProductResponse();
        BeanUtils.copyProperties(product, productResponse);
        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce Quantity {} for product id {}", quantity, productId);
        Product product=productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException("Product with given id not found", "PRODUCT_NOT_FOUND"));

        if(product.getQuantity()<quantity)
            throw new ProductServiceCustomException("Product does not have sufficient quantity to reduce", "INSUFFICIENT_QUANTITY");

        product.setQuantity(product.getQuantity()-quantity);
        productRepository.save(product);
        log.info("Product Quantity Updated Successfully");
    }
}
