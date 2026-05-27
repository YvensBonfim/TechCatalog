package com.yvens.techcatalog.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewPasswordDto {
    

    @NotBlank(message = "Campo obrigatorio")
    private String token;
     @NotBlank(message = "Campo obrigatorio")
     @Size(min = 8, message="Senha deve ter no minimo 8 caracteres" )
    private String password;

    


     public NewPasswordDto() {
    }

    
     public NewPasswordDto(String token, String password) {
        this.token = token;
        this.password = password;
    }


    

     public String getToken() {
         return token;
     }
     public String getPassword() {
         return password;
     }


     public void setToken(String token) {
         this.token = token;
     }


     public void setPassword(String password) {
         this.password = password;
     }

    
}
