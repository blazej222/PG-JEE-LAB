package org.example.post.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.example.post.entity.Post;
import org.example.category.repository.api.CategoryRepository;
import org.example.post.repository.api.PostRepository;
import org.example.user.entity.User;
import org.example.user.repository.api.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for all business actions regarding post entity.
 */
@ApplicationScoped
@NoArgsConstructor(force = true)
public class PostService {

    /**
     * Repository for post entity.
     */
    private final PostRepository postRepository;

    /**
     * Repository for category entity.
     */
    private final CategoryRepository categoryRepository;

    /**
     * Repository for user entity.
     */
    private final UserRepository userRepository;

    /**
     * @param postRepository  repository for post entity
     * @param categoryRepository repository for category entity
     * @param userRepository repository for user entity
     */
    @Inject
    public PostService(PostRepository postRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    /**
     * Finds single post.
     *
     * @param id post's id
     * @return container with post
     */
    public Optional<Post> find(UUID id) {
        return postRepository.find(id);
    }

    /**
     * @param id   post's id
     * @param user existing user
     * @return selected post for user
     */
    public Optional<Post> find(User user, UUID id) {
        return postRepository.findByIdAndUser(id, user);
    }

    /**
     * @return all available posts
     */
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    /**
     * @param user existing user, post's owner
     * @return all available posts of the selected user
     */
    public List<Post> findAll(User user) {
        return postRepository.findAllByUser(user);
    }

    /**
     * Creates new post.
     *
     * @param post new post
     */
    public void create(Post post) {
        postRepository.create(post);
    }

    /**
     * Updates existing post.
     *
     * @param post post to be updated
     */
    public void update(Post post) {
        postRepository.update(post);
    }

    /**
     * Deletes existing post.
     *
     * @param id existing post's id to be deleted
     */
    public void delete(UUID id) {
        postRepository.delete(postRepository.find(id).orElseThrow());
    }


//    public void updatePortrait(UUID id, InputStream is) {
//        postRepository.find(id).ifPresent(post -> {
//            try {
//                post.setPortrait(is.readAllBytes());
//                postRepository.update(post);
//            } catch (IOException ex) {
//                throw new IllegalStateException(ex);
//            }
//        });
//    } //FIXME: Avatar

    public Optional<List<Post>> findAllByCategory(UUID id) {
        return categoryRepository.find(id)
                .map(postRepository::findAllByCategory);
    }

    public Optional<List<Post>> findAllByUser(UUID id) {
        return userRepository.find(id)
                .map(postRepository::findAllByUser);
    }
}
