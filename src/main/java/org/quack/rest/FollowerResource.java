package org.quack.rest;

import org.jboss.logmanager.Logger;
import org.quack.domain.model.Follower;
import org.quack.domain.model.User;
import org.quack.domain.repository.FollowerRepository;
import org.quack.domain.repository.UserRepository;
import org.quack.rest.dto.FollowerRequest;
import org.quack.rest.dto.FollowerResponse;
import org.quack.rest.dto.FollowersPerUserResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

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

        if (id.equals(request.getFollowerId())) {
            LOGGER.info("UserId can't be equals FollowerId");
            return Response.status(Response.Status.CONFLICT).entity("You can't follow yourself").build();
        }

        User user = userRepository.findById(id);
        if (user == null) {
            LOGGER.info("User not found");
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

    @GET
    public Response listFollowers(@PathParam("userId") Long userId){

        User user = userRepository.findById(userId);
        if (user == null) {
            LOGGER.info("User not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var list = followerRepository.findByUser(userId);
        FollowersPerUserResponse responseObject = new FollowersPerUserResponse();
        responseObject.setFollowersCount(list.size());

        List<FollowerResponse> followerList = list.stream().map(FollowerResponse::new).collect(Collectors.toList());

        responseObject.setContent(followerList);

        LOGGER.info("List of Followers returned");
        return Response.ok(responseObject).build();
    }

    @DELETE
    @Transactional
    public Response unfollowUser(@PathParam("userId") Long userId, @QueryParam("followerId") Long followerId) {
        var user = userRepository.findById(userId);

        if (user == null) {
            LOGGER.info("User not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        followerRepository.deleteByFollowerAndUser(followerId, userId);

        LOGGER.info("Unfollow Success");
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
