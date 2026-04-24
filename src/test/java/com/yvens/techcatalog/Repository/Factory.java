package com.yvens.techcatalog.Repository;

import java.time.Instant;

import com.yvens.techcatalog.DTO.ProductDto;
import com.yvens.techcatalog.Entity.Category;
import com.yvens.techcatalog.Entity.Product;

public class Factory {
    
    public static Product createProduct(){
        Product product = new Product(1L, "phone", "good phone", 800.0, "https://img.com/img.png", Instant.parse("2020-07-14T10:00:00Z" ));
        product.getCategories().add(new Category(2L," Eletronics"));
        return product;

    }

     public static ProductDto createProductDto(){
        Product product = createProduct();
        return new ProductDto(product, product.getCategories());

     }
}
