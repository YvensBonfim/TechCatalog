package com.yvens.techcatalog.Service;

import com.yvens.techcatalog.Repository.CategoryRepositoty;
import com.yvens.techcatalog.Service.Exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yvens.techcatalog.DTO.CategoryDto;
import com.yvens.techcatalog.Entity.Category;

@Service
public class CategoryService {
    @Autowired
    public CategoryRepositoty repositoty;


    @Transactional(readOnly = true)
    public List<CategoryDto> findAll(){
        List<Category> list = repositoty.findAll();
        
        List<CategoryDto> listDto = list.stream().map(e->new CategoryDto(e)).collect(Collectors.toList());


        return listDto;
    }
    @Transactional(readOnly = true)
    public CategoryDto findById(Long id){

        Optional<Category> obj = repositoty.findById(id);
        Category entity =obj.orElseThrow(()->new EntityNotFoundException("Entity not found"));
        return new CategoryDto(entity);

    }
    
}
