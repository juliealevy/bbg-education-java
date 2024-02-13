package com.play.java.bbgeducation.api.endpoints;

public class InvalidApiEndpointLinkException extends RuntimeException{
    public InvalidApiEndpointLinkException(String method){
        super("Invalid api link for " + method);
    }
}
