package org.quack.rest;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.jboss.logmanager.Logger;
import org.quack.domain.model.User;
import org.quack.rest.dto.UserRequest;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(UserResource.class));

    @POST
    @Transactional
    public Response createUser( UserRequest userRequest ){
        User user = new User();
        user.setName(userRequest.getName());
        user.setAge(userRequest.getAge());
        user.persist();
        LOGGER.info("User created");
        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers(){
        PanacheQuery<User> query = User.findAll();
        LOGGER.info("Request successfully");
        return Response.ok(query.list()).build();
    }

}
