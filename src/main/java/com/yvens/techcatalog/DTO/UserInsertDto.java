package com.yvens.techcatalog.DTO;

import com.yvens.techcatalog.Service.UserInsertValid;

@UserInsertValid
public class UserInsertDto extends UserDto{

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
