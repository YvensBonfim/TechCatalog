package com.yvens.techcatalog.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yvens.techcatalog.Entity.Product;
import com.yvens.techcatalog.Repository.ProductRepositoty;

@Service
public class ProductService {

    public ProductRepositoty repositoty;


    public List<Product> findAll(){
      return  repositoty.findAll();
    }
    
}
