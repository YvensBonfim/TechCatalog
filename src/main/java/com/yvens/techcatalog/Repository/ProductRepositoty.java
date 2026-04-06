package com.yvens.techcatalog.Repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.yvens.techcatalog.Entity.Product;

public interface ProductRepositoty  extends JpaRepository<Product, Long>{

    
}
