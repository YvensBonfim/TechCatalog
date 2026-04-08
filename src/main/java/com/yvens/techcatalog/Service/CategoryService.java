package com.yvens.techcatalog.Service;

import com.yvens.techcatalog.Repository.CategoryRepositoty;
import com.yvens.techcatalog.Service.Exception.DataBaseException;
import com.yvens.techcatalog.Service.Exception.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yvens.techcatalog.DTO.CategoryDto;
import com.yvens.techcatalog.Entity.Category;

@Service
public class CategoryService {
    @Autowired
    public CategoryRepositoty repositoty;

    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {
        List<Category> list = repositoty.findAll();

        List<CategoryDto> listDto = list.stream().map(e -> new CategoryDto(e)).collect(Collectors.toList());

        return listDto;
    }

    @Transactional(readOnly = true)
    public CategoryDto findById(Long id) {

        Optional<Category> obj = repositoty.findById(id);
        Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new CategoryDto(entity);

    }

    @Transactional
    public CategoryDto insert(CategoryDto dto) {

        Category entity = new Category();
        entity.setName(dto.getName());
        entity = repositoty.save(entity);
        return new CategoryDto(entity);

    }

    @Transactional
    public CategoryDto update(CategoryDto dto, Long id) {
        try {
            Category entity = repositoty.getReferenceById(id);
            entity.setName(dto.getName());
            entity = repositoty.save(entity);
            return new CategoryDto(entity);
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

        }catch(DataIntegrityViolationException e){

           throw new DataBaseException("integrity violation");

        }

    }
}
