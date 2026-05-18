package com.yvens.techcatalog.Repository;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yvens.techcatalog.Entity.Product;
import com.yvens.techcatalog.Projection.ProductProjection;

public interface ProductRepositoty extends JpaRepository<Product, Long> {

    @Query(nativeQuery = true, 
        value = """
        SELECT * FROM(

            SELECT DISTINCT p.id, p.name 
            FROM tb_product p
            INNER JOIN tb_product_category pc ON pc.product_id = p.id
            WHERE (:categoryIds IS NULL OR pc.category_id IN :categoryIds)
            AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
            ) AS tb_result
        """,
        countQuery = """
            SELECT COUNT(DISTINCT p.id) 
            FROM tb_product p
            INNER JOIN tb_product_category pc ON pc.product_id = p.id
            WHERE (:categoryIds IS NULL OR pc.category_id IN :categoryIds)
            AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
        """)
    Page<ProductProjection> searchProducts(List<Long> categoryIds, String name, Pageable pageable);

    
    @Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj.id IN :productIds ")
    List<Product> searchProductswithcategories(List<Long> productIds);
}