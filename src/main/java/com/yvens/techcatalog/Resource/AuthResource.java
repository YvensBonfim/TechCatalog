package com.yvens.techcatalog.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.yvens.techcatalog.DTO.EmailDto;
import com.yvens.techcatalog.DTO.NewPasswordDto;
import com.yvens.techcatalog.DTO.ProductDto;
import com.yvens.techcatalog.DTO.UserDto;
import com.yvens.techcatalog.DTO.UserInsertDto;
import com.yvens.techcatalog.DTO.UserUpdateDto;
import com.yvens.techcatalog.Service.AuthService;
import com.yvens.techcatalog.Service.UserService;

import jakarta.validation.Valid;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

    @Autowired
    private AuthService authservice;

 
    @PostMapping(value = "/recover-token")
    public ResponseEntity<Void> createRecoveryToken(@Valid @RequestBody EmailDto body)

    {
        authservice.createRecoveryToken(body);
        return ResponseEntity.noContent().build();

    }
    @PutMapping(value = "/new-password")
    public ResponseEntity<Void> saveNewPassword(@Valid @RequestBody NewPasswordDto body)

    {
        authservice.saveNewPassword(body);
        return ResponseEntity.noContent().build();

    }


}
