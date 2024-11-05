package org.example.user.services;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.example.post.entity.Post;
import org.example.post.repository.api.PostRepository;
import org.example.user.entity.User;
import org.example.user.entity.UserRoles;
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

@LocalBean
@Stateless
@NoArgsConstructor(force = true)
public class UserService {

    private final UserRepository repository;
    private final PostRepository postRepository;

    private final Pbkdf2PasswordHash passwordHash;

    @Inject
    public UserService(UserRepository repository, PostRepository postRepository, @SuppressWarnings("CdiInjectionPointsInspection") Pbkdf2PasswordHash passwordHash) {
        this.repository = repository;
        this.postRepository = postRepository;
        this.passwordHash = passwordHash;
    }

    @RolesAllowed(UserRoles.ADMIN)
    public List<User> findAll() {
        return repository.findAll();
    }

    @RolesAllowed(UserRoles.ADMIN)
    public Optional<User> find(UUID id) {
        return repository.find(id);
    }

    @RolesAllowed(UserRoles.ADMIN)
    public Optional<User> find(String name) {
        return repository.findByName(name);
    }

    @PermitAll
    public void create(User user) {
        user.setPassword(passwordHash.generate(user.getPassword().toCharArray()));
        user.setRole(UserRoles.USER);
        repository.create(user);
    }

    public void update(User user) {
        repository.update(user);
    }

    @RolesAllowed(UserRoles.ADMIN)
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

    @PermitAll
    public boolean verify(String login, String password) {
        return find(login)
                .map(user -> passwordHash.verify(password.toCharArray(), user.getPassword()))
                .orElse(false);
    }


}
