package org.quack.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.jboss.logmanager.Logger;
import org.quack.domain.model.User;
import org.quack.domain.repository.UserRepository;
import org.quack.rest.dto.UserRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(UserResource.class));
    private final UserRepository userRepository;
    private final Validator validator;

    @Inject
    public UserResource(UserRepository userRepository, Validator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @POST
    @Transactional
    public Response createUser( UserRequest userRequest ){

        Set<ConstraintViolation<UserRequest>> validate = validator.validate(userRequest);

        if (!validate.isEmpty()) {
            ConstraintViolation<UserRequest> userRequestConstraintViolation = validate.stream().findAny().get();
            String message = userRequestConstraintViolation.getMessage();
            return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
        }

        User user = new User();
        user.setName(userRequest.getName());
        user.setAge(userRequest.getAge());
        userRepository.persist(user);
        LOGGER.info("User created");
        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers(){
        PanacheQuery<User> query = userRepository.findAll();
        LOGGER.info("Request successfully");
        return Response.ok(query.list()).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {
        User user = userRepository.findById(id);
        if (user != null) {
            userRepository.delete(user);
            LOGGER.info("User with id n:" + id + " deleted successfully");
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, UserRequest userRequest) {
        User user = userRepository.findById(id);
        if (user != null) {
            user.setName(userRequest.getName());
            user.setAge(userRequest.getAge());
            LOGGER.info("User with id n:" + id + " updated successfully");
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
