package org.example.post.repository.api;

import org.example.post.entity.Category;
import org.example.post.entity.Post;
import org.example.repository.api.Repository;
import org.example.user.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends Repository<Post, UUID> {

    Optional<Post> findByIdAndUser(UUID id, User user);

    List<Post> findAllByUser(User user);

    List<Post> findAllByCategory(Category category);

}
