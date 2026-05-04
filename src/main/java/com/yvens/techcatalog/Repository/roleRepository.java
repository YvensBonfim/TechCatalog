package com.yvens.techcatalog.Repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.yvens.techcatalog.Entity.Role;


public interface roleRepository  extends JpaRepository<Role, Long>{

    
}
