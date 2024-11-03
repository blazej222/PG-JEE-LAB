package org.example.user.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.example.post.entity.Post;
import org.example.post.repository.api.PostRepository;
import org.example.user.entity.User;
import org.example.user.repository.api.UserRepository;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.NotAllowedException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class UserService {

    private final UserRepository repository;
    private final PostRepository postRepository;

    @Inject
    public UserService(UserRepository repository, PostRepository postRepository) {
        this.repository = repository;
        this.postRepository = postRepository;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> find(UUID id) {
        return repository.find(id);
    }

    public Optional<User> find(String name) {
        return repository.findByName(name);
    }

    public void create(User user) {
        repository.create(user);
    }


    public void update(User user) {
        repository.update(user);
    }

    public void delete(UUID id) {
        User tmp = repository.find(id).orElseThrow(NotFoundException::new);
        List<Post> posts = postRepository.findAllByUser(tmp);
        for (Post post : posts) {
            postRepository.delete(post);
        }
        repository.delete(tmp);
    }

    public void createAvatar(UUID id, InputStream avatar, String pathToAvatars) throws NotAllowedException {
        repository.find(id).ifPresent(user -> {
            try {
                Path destinationPath = Path.of(pathToAvatars, id.toString() + ".png");
                if (Files.exists(destinationPath)) {
                    throw new NotAllowedException("Avatar already exists");
                }
                Files.copy(avatar, destinationPath);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        });

    }

    public void updateAvatar(UUID id, InputStream avatar, String pathToAvatars) {
        repository.find(id).ifPresent(user -> {
            try {
                Path existingPath = Path.of(pathToAvatars, id.toString() + ".png");
                if (Files.exists(existingPath)) {
                    Files.copy(avatar, existingPath, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    throw new NotFoundException("Avatar not found");
                }
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        });

    }

}
