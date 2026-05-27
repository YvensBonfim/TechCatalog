package com.yvens.techcatalog.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailDto {

    @NotBlank(message = "Campo obrigatorio")
    @Email(message = "Email invalido")
    private String email;

  
    public EmailDto() {
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }
}