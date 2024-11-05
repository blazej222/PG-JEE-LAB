package org.example.post.service;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJBAccessException;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.example.post.entity.Post;
import org.example.category.repository.api.CategoryRepository;
import org.example.post.repository.api.PostRepository;
import org.example.user.entity.User;
import org.example.user.entity.UserRoles;
import org.example.user.repository.api.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for all business actions regarding post entity.
 */
@LocalBean
@Stateless
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

    private final SecurityContext securityContext;

    /**
     * @param postRepository  repository for post entity
     * @param categoryRepository repository for category entity
     * @param userRepository repository for user entity
     */
    @Inject
    public PostService(PostRepository postRepository, CategoryRepository categoryRepository, UserRepository userRepository,
    @SuppressWarnings("CdiInjectionPointsInspection") SecurityContext securityContext) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.securityContext = securityContext;
    }

    /**
     * Finds single post.
     *
     * @param id post's id
     * @return container with post
     */
    @RolesAllowed({UserRoles.USER,UserRoles.ADMIN})
    public Optional<Post> find(UUID id) {
        return postRepository.find(id);
    }

    @RolesAllowed({UserRoles.USER,UserRoles.ADMIN})
    public Optional<Post> find(User user,UUID id) {
        return postRepository.findByIdAndUser(id,user);
    }

    /**
     * @return all available posts
     */
    @RolesAllowed({UserRoles.USER,UserRoles.ADMIN})
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    /**
     * @param user existing user, post's owner
     * @return all available posts of the selected user
     */
    @RolesAllowed({UserRoles.USER,UserRoles.ADMIN})
    public List<Post> findAll(User user) {
        return postRepository.findAllByUser(user);
    }

    @RolesAllowed({UserRoles.USER,UserRoles.ADMIN})
    public List<Post> findAllForCallerPrincipal() {
        if(securityContext.isCallerInRole(UserRoles.ADMIN)){
            return postRepository.findAll();
        }
        User user = userRepository.findByName(securityContext.getCallerPrincipal().getName()).orElseThrow(IllegalStateException::new);
        return findAll(user);
    }

    @RolesAllowed({UserRoles.USER,UserRoles.ADMIN})
    public Optional<Post> findForCallerPrincipal(UUID id) {
        Post post = find(id).orElseThrow(IllegalStateException::new);

        checkAdminRoleOrOwner(Optional.ofNullable(post));

        return Optional.ofNullable(post);

//        if(securityContext.isCallerInRole(UserRoles.ADMIN)){
//            return find(id);
//        }
//        User user = userRepository.findByName(securityContext.getCallerPrincipal().getName()).orElseThrow(IllegalStateException::new);
//        return find(user,id);
    }

    /**
     * Creates new post.
     *
     * @param post new post
     */
    @RolesAllowed(UserRoles.ADMIN)
    public void create(Post post) {
        postRepository.create(post);
    }

    @RolesAllowed(UserRoles.USER)
    public void createForCallerPrincipal(Post post) {
        User user = userRepository.findByName(securityContext.getCallerPrincipal().getName()).orElseThrow(IllegalStateException::new);
        post.setUser(user);
        postRepository.create(post);
    }

    /**
     * Updates existing post.
     *
     * @param post post to be updated
     */

    @RolesAllowed({UserRoles.USER,UserRoles.ADMIN})
    public void update(Post post) {
        checkAdminRoleOrOwner(postRepository.find(post.getId()));
        postRepository.update(post);
    }

    /**
     * Deletes existing post.
     *
     * @param id existing post's id to be deleted
     */

    @RolesAllowed({UserRoles.USER,UserRoles.ADMIN})
    public void delete(UUID id) {
        checkAdminRoleOrOwner(postRepository.find(id));
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

    @RolesAllowed(UserRoles.USER)
    public Optional<List<Post>> findAllByCategory(UUID id) {
        return categoryRepository.find(id)
                .map(postRepository::findAllByCategory);
    }

    @RolesAllowed(UserRoles.USER)
    public Optional<List<Post>> findAllByUser(UUID id) {
        return userRepository.find(id)
                .map(postRepository::findAllByUser);
    }

    /**
     * @param post post to be checked
     * @throws EJBAccessException when caller principal has no admin role and is not character's owner
     */
    private void checkAdminRoleOrOwner(Optional<Post> post) throws EJBAccessException {
        if (securityContext.isCallerInRole(UserRoles.ADMIN)) {
            return;
        }
        if (securityContext.isCallerInRole(UserRoles.USER)
                && post.isPresent()
                && post.get().getUser().getName().equals(securityContext.getCallerPrincipal().getName())) {
            return;
        }
        throw new EJBAccessException("Caller not authorized.");
    }

}
