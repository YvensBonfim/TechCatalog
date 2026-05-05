package com.yvens.techcatalog.Resource.Exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

    private List<FieldMessage> errors = new ArrayList<>();

    public List<FieldMessage> getErros() {
        return errors;
    }


    public void addError(String fieldNaame, String message){
        errors.add(new FieldMessage(fieldNaame, message));
    }
    
}
