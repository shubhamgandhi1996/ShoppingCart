package com.shubhamgandhi.ProductService.controller;

import com.shubhamgandhi.ProductService.model.ProductRequest;
import com.shubhamgandhi.ProductService.model.ProductResponse;
import com.shubhamgandhi.ProductService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasAuthority('Admin')") // so only be allowed when authority is admin
    @PostMapping
    public ResponseEntity<Long> addProduct(@RequestBody ProductRequest productRequest){
        long productId=productService.addProduct(productRequest);
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    //we need a model to send the product back according to the id provided
    //we needed to allow ppl to access this request having any one of these roles
    //internal scopes req comes when we are calling from one security to another security
    //internal is SCOPE not the ROLE
    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer') || hasAuthority('SCOPE_internal')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable long id){
        ProductResponse productResponse=productService.getProductById(id);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PutMapping("/reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity(@PathVariable(name = "id") long productId, @RequestParam long quantity){
        productService.reduceQuantity(productId, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
