package com.yvens.techcatalog.Service;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.yvens.techcatalog.DTO.UserInsertDto;
import com.yvens.techcatalog.Entity.User;
import com.yvens.techcatalog.Repository.UserRepository;
import com.yvens.techcatalog.Resource.Exceptions.FieldMessage;

// CORREÇÃO: Implementar ConstraintValidator e não ConstraintDescriptor
public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDto> {
    
    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDto dto, ConstraintValidatorContext context) {
        
        List<FieldMessage> list = new ArrayList<>();
        
     
        User user = repository.findByEmail(dto.getEmail());
        if (user != null) {
            list.add(new FieldMessage("email", "Este email já está cadastrado"));
        }
        
        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                   .addPropertyNode(e.getFieldName())
                   .addConstraintViolation();
        }
        return list.isEmpty();
    }
}