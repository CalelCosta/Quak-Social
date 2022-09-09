package org.quack.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import org.quack.domain.model.Follower;
import org.quack.domain.model.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean follows(User userFollower, User user) {

        var parameters = Parameters
                .with("userFollower", userFollower)
                .and("user", user).map();

        PanacheQuery<Follower> query = find("userFollower = :userFollower and user = :user", parameters);
        Optional<Follower> result = query.firstResultOptional();

        return result.isPresent();
    }

    public List<Follower> findByUser(Long userId) {
        PanacheQuery<Follower> query = find("user.id", userId);
        return query.list();
    }

    public void deleteByFollowerAndUser(Long followerId, Long userId) {
        var params = Parameters
                .with("userId", userId)
                .and("userFollowerId", followerId)
                .map();

        delete("userFollower.id = :userFollowerId and user.id = :userId", params);
    }
}
