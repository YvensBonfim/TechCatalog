package com.yvens.techcatalog.Service;

import com.yvens.techcatalog.Repository.UserRepository;
import com.yvens.techcatalog.Repository.roleRepository;
import com.yvens.techcatalog.Service.Exception.DataBaseException;
import com.yvens.techcatalog.Service.Exception.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;

import com.yvens.techcatalog.DTO.RoleDto;
import com.yvens.techcatalog.DTO.UserDto;
import com.yvens.techcatalog.DTO.UserInsertDto;
import com.yvens.techcatalog.DTO.UserUpdateDto;
import com.yvens.techcatalog.Entity.Role;
import com.yvens.techcatalog.Entity.User;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserRepository repositoty;

    @Autowired
   private roleRepository roleRepository;

  

    @Transactional(readOnly = true)
    public Page<UserDto> findAllPaged(Pageable pageable) {
        Page<User> list = repositoty.findAll(pageable);

        return list.map(x -> new UserDto(x));

    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {

        Optional<User> obj = repositoty.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new UserDto(entity);

    }

    @Transactional
    public UserDto insert(UserInsertDto dto) {

        User entity = new User();
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        copyDtoToEntity(dto, entity);
        entity = repositoty.save(entity);
        return new UserDto(entity);

    }

    @Transactional
    public UserDto update(UserUpdateDto dto, Long id) {
        try {
            User entity = repositoty.getReferenceById(id);
             copyDtoToEntity(dto, entity);
            entity = repositoty.save(entity);
            return new UserDto(entity);
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

    private void copyDtoToEntity(UserDto dto, User entity) {

        entity.setFirstName(dto.getFirstName());

        entity.setLastName(dto.getLastName());

        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();
        for (RoleDto roleDto : dto.getRoles()) {
            Role role =roleRepository.getReferenceById(roleDto.getId());
            entity.getRoles().add(role);

        }
    }

}
