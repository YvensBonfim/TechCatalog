package com.yvens.techcatalog.Repository;



import org.springframework.data.jpa.repository.JpaRepository;


import com.yvens.techcatalog.Entity.User;



public interface UserRepository  extends JpaRepository<User, Long>{
 
    User findByEmail(String email);
    
}
 