package com.play.java.bbgeducation.api.users.links;

import com.play.java.bbgeducation.api.links.ApiLinkProviderBase;
import com.play.java.bbgeducation.api.links.ApiLinkService;
import com.play.java.bbgeducation.api.users.GetUserByEmailRequest;
import com.play.java.bbgeducation.api.users.UpdateUserRequest;
import com.play.java.bbgeducation.api.users.UserController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserApiLinkProvider extends ApiLinkProviderBase<Class<UserController>> {
    public UserApiLinkProvider(ApiLinkService apiLinkService){
        super(apiLinkService, UserController.class);

    }

    public Link getUpdateApiLink(){
        return getApiLink(UserLinkRelations.UPDATE.value, ApiUpdateUserRequest.getApiBody(),
                "updateUser", Long.class, UpdateUserRequest.class, HttpServletRequest.class);
    }

    public Link getAllApiLink(){
        return getApiLink(UserLinkRelations.GET_ALL.value,
                "getAll",HttpServletRequest.class);
    }

    public Link getByIdApiLink(){
        return getApiLink(UserLinkRelations.GET_BY_ID.value,
                "getById", Long.class);
    }

    public Link getByEmailApiLink(){
        return getApiLink(UserLinkRelations.GET_BY_EMAIL.value,ApiGetUserByEmailRequest.getApiBody(),
                "getByEmail", GetUserByEmailRequest.class, HttpServletRequest.class);
    }

    public Link getDeleteApiLink(){
        return getApiLink(UserLinkRelations.DELETE.value,
                "deleteById", Long.class,HttpServletRequest.class);
    }
    @Override
    public List<Link> getAllLinks() {
        return List.of(
                getUpdateApiLink(),
                getAllApiLink(),
                getByIdApiLink(),
                getByEmailApiLink(),
                getDeleteApiLink()
        );
    }
}
