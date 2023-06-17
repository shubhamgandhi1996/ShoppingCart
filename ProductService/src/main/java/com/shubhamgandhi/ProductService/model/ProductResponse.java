package com.shubhamgandhi.ProductService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder //so that to use this class with builder pattern to build object
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String productName;
    private long productId;
    private long quantity;
    private long price;
}
