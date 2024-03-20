package com.play.java.bbgeducation.api.common;

public enum ApiBodyType {
    STRING(String.class.getSimpleName().toLowerCase()),
    BOOLEAN(Boolean.class.getSimpleName().toLowerCase()),
    DATE("MM-dd-yyyy"),
    INTEGER(Integer.class.getSimpleName().toLowerCase())
    ;

public final String value;
private ApiBodyType(String value){
    this.value = value;
}
}
