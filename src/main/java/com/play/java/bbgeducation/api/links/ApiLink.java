package com.play.java.bbgeducation.api.links;

import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;

@Data
public class ApiLink extends Link {
    private String httpMethod;
    private Object body;

    public ApiLink(String linkRelation, String href, String httpMethod){
        super(href, LinkRelation.of(linkRelation));
        this.httpMethod = httpMethod;
    }

    public void AddBody(Object body){
        this.body = body;
    }
    @Override
    public String toString(){
        return super.toString() + ";httpMethod=" + httpMethod;
    }
}
