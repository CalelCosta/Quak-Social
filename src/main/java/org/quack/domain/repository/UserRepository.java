package org.quack.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.quack.domain.model.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
}
