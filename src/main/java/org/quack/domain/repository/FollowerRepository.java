package org.quack.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.quack.domain.model.Follower;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {
}
