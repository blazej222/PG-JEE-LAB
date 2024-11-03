package org.example.post.repository.memory;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.example.datastore.component.DataStore;
import org.example.category.entity.Category;
import org.example.post.entity.Post;
import org.example.post.repository.api.PostRepository;
import org.example.user.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestScoped
public class PostInMemoryRepository implements PostRepository {
    /**
     * Underlying data store. In future should be replaced with database connection.
     */
    private final DataStore store;

    /**
     * @param store data store
     */
    @Inject
    public PostInMemoryRepository(DataStore store) {
        this.store = store;
    }

    @Override
    public Optional<Post> find(UUID id) {
        return store.findAllPosts().stream()
                .filter(post -> post.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Post> findAll() {
        return store.findAllPosts();
    }

    @Override
    public void create(Post entity) {
        store.createPost(entity);
    }

    @Override
    public void delete(Post entity) {
        store.deletePost(entity.getId());
    }

    @Override
    public void update(Post entity) {
        store.updatePost(entity);
    }

    @Override
    public Optional<Post> findByIdAndUser(UUID id, User user) {
        return store.findAllPosts().stream()
                .filter(post -> post.getUser().equals(user))
                .filter(post -> post.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Post> findAllByUser(User user) {
        return store.findAllPosts().stream()
                .filter(post -> user.equals(post.getUser()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findAllByCategory(Category category) {
        return store.findAllPosts().stream()
                .filter(post -> category.equals(post.getCategory()))
                .collect(Collectors.toList());
    }

}
