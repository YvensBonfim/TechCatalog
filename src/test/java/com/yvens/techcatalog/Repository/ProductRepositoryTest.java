package com.yvens.techcatalog.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.yvens.techcatalog.Entity.Product;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepositoty repository;

    private long existingId;
     private long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        countTotalProducts = 25L;
        nonExistingId =100L;
        

    }

    @Test
    public void saveShouldPersistWithAutoIncrement() {

        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);
        assertNotNull(product.getId());
        assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void findByIdShouldreturnOptionalNotvoid() {
        Product product = Factory.createProduct();
        product = repository.save(product);

        Optional<Product> result = repository.findById(product.getId());

        assertTrue(result.isPresent());
        assertEquals(product.getName(), result.get().getName());

    }
    @Test
    public void findByIdShouldreturnvoid() {
        Product product = new Product();
        product.setId(nonExistingId);

        Optional<Product> result = repository.findById(product.getId());
 
        assertTrue(result.isEmpty());
      

    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);

        assertFalse(result.isPresent());

    }

}
