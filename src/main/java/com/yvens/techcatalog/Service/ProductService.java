package com.yvens.techcatalog.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yvens.techcatalog.Entity.Product;
import com.yvens.techcatalog.Repository.ProductRepositoty;

@Service
public class ProductService {
    @Autowired
    public ProductRepositoty repositoty;

  @Transactional(readOnly = true)
    public List<Product> findAll(){
      return  repositoty.findAll();
    }
    
}
