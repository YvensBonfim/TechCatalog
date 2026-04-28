package com.yvens.techcatalog.Service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.yvens.techcatalog.DTO.ProductDto;
import com.yvens.techcatalog.Entity.Category;
import com.yvens.techcatalog.Entity.Product;
import com.yvens.techcatalog.Repository.CategoryRepositoty;
import com.yvens.techcatalog.Repository.Factory;
import com.yvens.techcatalog.Repository.ProductRepositoty;
import com.yvens.techcatalog.Service.Exception.DataBaseException;
import com.yvens.techcatalog.Service.Exception.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepositoty repositoty;

    @Mock
    private CategoryRepositoty catrepositoty;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private Category category;
    private ProductDto prodDto;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 3L;
        product = Factory.createProduct();
        category = Factory.creaCategory();
         prodDto= Factory.createProductDto();
        page = new PageImpl<>(List.of(product));
       

        Mockito.when(repositoty.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(repositoty.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repositoty.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(repositoty.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(repositoty.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(catrepositoty.getReferenceById(existingId)).thenReturn(category);
        Mockito.when(catrepositoty.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(repositoty.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.doNothing().when(repositoty).deleteById(existingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repositoty).deleteById(dependentId);

        Mockito.when(repositoty.existsById(existingId)).thenReturn(true);
        // Mockito.when(repositoty.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(repositoty.existsById(dependentId)).thenReturn(true);

        Mockito.when(repositoty.existsById(existingId)).thenReturn(true);
        Mockito.when(repositoty.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(repositoty.existsById(dependentId)).thenReturn(true);

    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExists() {
       

        ProductDto result = service.update(prodDto, existingId);

        Assertions.assertNotNull(result);

        Mockito.verify(repositoty, Mockito.times(1)).getReferenceById(existingId);

    }

      @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdnonExist() {
        assertThrows(ResourceNotFoundException.class, () -> {
            ProductDto result = service.update(prodDto, nonExistingId);
        });
        
    }

    @Test
    public void findByIdShouldReturnProductDtoWhenIdExists() {

        ProductDto result = service.findById(existingId);
        Assertions.assertNotNull(result);
        Mockito.verify(repositoty, Mockito.times(1)).findById(existingId);

    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdnonExist() {
        assertThrows(ResourceNotFoundException.class, () -> {
            ProductDto result = service.findById(nonExistingId);
        });
        Mockito.verify(repositoty).findById(nonExistingId);
    }

    @Test
    public void findAllShouldreturnPage() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDto> result = service.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repositoty, Mockito.times(1)).findAll(pageable);

    }

    @Test
    public void deleteShouldTrowDatabaseExceptionWhenDependentId() {
        assertThrows(DataBaseException.class, () -> {
            service.delete(dependentId);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesnotExist() {
        assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });

    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

    }

}
