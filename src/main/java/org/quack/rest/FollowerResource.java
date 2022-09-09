package org.quack.rest;

import org.jboss.logmanager.Logger;
import org.quack.domain.model.Follower;
import org.quack.domain.model.User;
import org.quack.domain.repository.FollowerRepository;
import org.quack.domain.repository.UserRepository;
import org.quack.rest.dto.FollowerRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(FollowerResource.class));
    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;

    @Inject
    public FollowerResource(FollowerRepository followerRepository, UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    @PUT
    @Transactional
    public Response followUser(@PathParam("userId") Long id, FollowerRequest request) {
        User user = userRepository.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User follower = userRepository.findById(request.getFollowerId());

        boolean follows = followerRepository.follows(follower, user);

        if (!follows) {
            var entity = new Follower();
            entity.setUser(user);
            entity.setUserFollower(follower);

            followerRepository.persist(entity);
            LOGGER.info("You follow " + user + " now");
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
