package com.shubhamgandhi.ProductService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//SINCce we are using the lombork no need to define the getters and setters just add the data annotations
//all the getters setters consturctor will get added for you open the annotaions and u can see
@Entity
@Data
@AllArgsConstructor //it willcreate all args const
@NoArgsConstructor //it will create the no args const or else erroe
@Builder //it will give the builder pattern implementaion of these class
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long productId;
    @Column(name="PRODUCT_NAME")
    private String productName;
    @Column(name="PRICE")
    private long price;
    @Column(name="QUANTITY")
    private long quantity;
}
