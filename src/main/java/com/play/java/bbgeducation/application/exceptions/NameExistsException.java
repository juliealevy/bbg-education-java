package com.play.java.bbgeducation.application.exceptions;

import javax.naming.Name;

public class NameExistsException extends Exception {
    public NameExistsException(String entityName)  {
        super(entityName + " name already exists");
    }
}
