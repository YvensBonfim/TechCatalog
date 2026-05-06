package com.yvens.techcatalog.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.yvens.techcatalog.DTO.UserUpdateDto;
import com.yvens.techcatalog.Entity.User;
import com.yvens.techcatalog.Repository.UserRepository;
import com.yvens.techcatalog.Resource.Exceptions.FieldMessage;

// CORREÇÃO: Implementar ConstraintValidator e não ConstraintDescriptor
public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDto> {
    
    @Autowired
    private UserRepository repository;

    @Autowired
    private HttpServletRequest request;
  
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserUpdateDto dto, ConstraintValidatorContext context) {

      @SuppressWarnings("unchecked")
        var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        
        long userId = Long.parseLong(uriVars.get("id"));
        List<FieldMessage> list = new ArrayList<>();
        
     
        User user = repository.findByEmail(dto.getEmail());
        if (user != null && userId!=user.getId()) {
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