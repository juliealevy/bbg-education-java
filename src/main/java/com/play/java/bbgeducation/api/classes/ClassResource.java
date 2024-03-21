package com.play.java.bbgeducation.api.classes;

import com.play.java.bbgeducation.api.common.RestResource;
import com.play.java.bbgeducation.api.endpoints.HasApiEndpoints;
import org.springframework.web.bind.annotation.RequestMapping;

@RestResource
@RequestMapping("api/programs/{pid}/sessions/{sid}/courses/{cid}/classes")
@HasApiEndpoints
public class ClassResource {

    //get all classes for a session course
    //get a specific class for a session course

    //add a class to a session course
    //remove a class from a session course
    //edit a class

}
