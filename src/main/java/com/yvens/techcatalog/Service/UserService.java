package com.yvens.techcatalog.Service;

import com.yvens.techcatalog.Repository.UserRepository;
import com.yvens.techcatalog.Repository.roleRepository;
import com.yvens.techcatalog.Service.Exception.DataBaseException;
import com.yvens.techcatalog.Service.Exception.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.yvens.techcatalog.Projection.UserDetailsProjection;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserRepository repository;

    @Autowired
   private roleRepository rolerepository;

  

    @Transactional(readOnly = true)
    public Page<UserDto> findAllPaged(Pageable pageable) {
        Page<User> list = repository.findAll(pageable);

        return list.map(x -> new UserDto(x));

    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {

        Optional<User> obj = repository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new UserDto(entity);

    }

    @Transactional
    public UserDto insert(UserInsertDto dto) {

        User entity = new User();
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        copyDtoToEntity(dto, entity);
        entity.getRoles().clear();
        Role role =rolerepository.findByAuthority("ROLE_OPERATOR");
        entity.getRoles().add(role);
        entity = repository.save(entity);
        return new UserDto(entity);

    }

    @Transactional
    public UserDto update(UserUpdateDto dto, Long id) {
        try {
            User entity = repository.getReferenceById(id);
             copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new UserDto(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("id not found" + id);

        }

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {

        if (!repository.existsById(id)) {

            throw new ResourceNotFoundException("Id not found");
        }
        try {
            repository.deleteById(id);

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
            Role role =rolerepository.getReferenceById(roleDto.getId());
            entity.getRoles().add(role);

        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      
      
	
		
		List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
		if (result.size() == 0) {
			throw new UsernameNotFoundException("Email not found");
		}
		
		User user = new User();
		user.setEmail(result.get(0).getUsername());
		user.setPassword(result.get(0).getPassword());
		for (UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}
		
		return user;
    }
    }


