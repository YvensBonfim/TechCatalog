package com.yvens.techcatalog.Repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.yvens.techcatalog.Entity.Category;

public interface CategoryRepositoty  extends JpaRepository<Category, Long>{

    
}
