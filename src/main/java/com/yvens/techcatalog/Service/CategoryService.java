package com.yvens.techcatalog.Service;

import com.yvens.techcatalog.Repository.CategoryRepositoty;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yvens.techcatalog.Entity.Category;

@Service
public class CategoryService {
    @Autowired
    public CategoryRepositoty repositoty;



    public List<Category> findAll(){
         return repositoty.findAll();
    }
    
}
