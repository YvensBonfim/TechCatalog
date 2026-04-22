package com.yvens.techcatalog.Service;

import com.yvens.techcatalog.Repository.CategoryRepositoty;
import com.yvens.techcatalog.Repository.ProductRepositoty;
import com.yvens.techcatalog.Service.Exception.DataBaseException;
import com.yvens.techcatalog.Service.Exception.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;

import com.yvens.techcatalog.DTO.CategoryDto;
import com.yvens.techcatalog.DTO.ProductDto;
import com.yvens.techcatalog.Entity.Category;
import com.yvens.techcatalog.Entity.Product;

@Service
public class ProductService {

    @Autowired
    public ProductRepositoty repositoty;

    @Autowired
    public CategoryRepositoty categoryRepositoty;

    @Transactional(readOnly = true)
    public Page<ProductDto> findAllPaged(Pageable pageable) {
        Page<Product> list = repositoty.findAll(pageable);

        return list.map(x -> new ProductDto(x));

    }

    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {

        Optional<Product> obj = repositoty.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDto(entity, entity.getCategories());

    }

    @Transactional
    public ProductDto insert(ProductDto dto) {

        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repositoty.save(entity);
        return new ProductDto(entity,entity.getCategories());

    }

    @Transactional
    public ProductDto update(ProductDto dto, Long id) {
        try {
            Product entity = repositoty.getReferenceById(id);
             copyDtoToEntity(dto, entity);
            entity = repositoty.save(entity);
            return new ProductDto(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("id not found" + id);

        }

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {

        if (!repositoty.existsById(id)) {

            throw new ResourceNotFoundException("Id not found");
        }
        try {
            repositoty.deleteById(id);

        } catch (DataIntegrityViolationException e) {

            throw new DataBaseException("integrity violation");

        }

    }

    private void copyDtoToEntity(ProductDto dto, Product entity) {

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();
        for (CategoryDto catDto : dto.getCategories()) {
            Category category = categoryRepositoty.getReferenceById(catDto.getId());
            entity.getCategories().add(category);

        }
    }

}
