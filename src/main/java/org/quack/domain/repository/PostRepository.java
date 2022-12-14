package org.quack.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.quack.domain.model.Post;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {
}
