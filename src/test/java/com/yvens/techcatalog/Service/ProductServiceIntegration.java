package com.yvens.techcatalog.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.yvens.techcatalog.DTO.ProductDto;
import com.yvens.techcatalog.Repository.ProductRepositoty;
import com.yvens.techcatalog.Service.Exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Transactional
@SpringBootTest
public class ProductServiceIntegration {

    @Autowired
    private ProductService service;
    @Autowired
    private ProductRepositoty repository;

    private long existingId;
    private long nonExistingId;
    private long countTotalProduct;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProduct = 25L;

    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExist() {

        service.delete(existingId);

        assertEquals(countTotalProduct - 1, repository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesnotExist() {

        assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });

    }

    @Test
    public void findAllPagedShouldReturnPage(){
        

      PageRequest pageRequest = PageRequest.of(0,10);

        Page<ProductDto> result =service.findAllPaged(pageRequest);

        assertFalse(result.isEmpty());
        assertEquals(0,result.getNumber());
        assertEquals(10, result.getSize());
        assertEquals(countTotalProduct, result.getTotalElements());

    }
    @Test
    public void findAllPagedShouldReturnEmptyPagewhenPagedoesntExist(){
        

      PageRequest pageRequest = PageRequest.of(0,10);

        Page<ProductDto> result =service.findAllPaged(pageRequest);

        assertTrue(result.isEmpty());
    
    }
    @Test
    public void findAllPagedShouldReturnOrderedPageWhenSortedByName(){
        

      PageRequest pageRequest = PageRequest.of(0,10,Sort.by("name"));

        Page<ProductDto> result =service.findAllPaged(pageRequest);

        assertFalse(result.isEmpty());
        assertEquals("Macbook Pro", result.getContent().get(0).getName());
        assertEquals("PC Gamer", result.getContent().get(1).getName());
        assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    
    }


}
