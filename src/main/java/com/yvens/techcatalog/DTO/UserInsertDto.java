package com.yvens.techcatalog.DTO;

import com.yvens.techcatalog.Service.UserInsertValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@UserInsertValid
public class UserInsertDto extends UserDto{

    @NotBlank(message = "campo obrigatorio")
    @Size(min=8, message="deve ter no minimo 8 caracteres")
    private String password;



    public UserInsertDto (){
        super();

    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    


    
}
