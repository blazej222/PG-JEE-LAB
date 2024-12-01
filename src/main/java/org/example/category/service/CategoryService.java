package org.example.category.service;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.example.category.entity.Category;
import org.example.post.entity.Post;
import org.example.category.repository.api.CategoryRepository;
import org.example.post.repository.api.PostRepository;
import jakarta.ws.rs.NotFoundException;
import org.example.user.entity.UserRoles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@LocalBean
@Stateless
@NoArgsConstructor(force = true)
public class CategoryService {
        /**
         * Repository for category entity.
         */
        private final CategoryRepository repository;
        private final PostRepository postRepository;

        private final SecurityContext securityContext;

        /**
         * @param repository repository for category entity
         */
        @Inject
        public CategoryService(CategoryRepository repository, PostRepository postRepository,
                               @SuppressWarnings("CdiInjectionPointsInspection") SecurityContext securityContext) {
            this.repository = repository;
            this.postRepository = postRepository;
            this.securityContext = securityContext;
        }

        public boolean verify()
        {
            boolean result = securityContext.isCallerInRole(UserRoles.USER);
            System.out.println(result);
            return result;
        }
        /**
         * @param id category's id
         * @return container with category entity
         */
        @RolesAllowed({UserRoles.ADMIN,UserRoles.USER})
        public Optional<Category> find(UUID id) {
            Optional<Category> result = repository.find(id);

            if (result.isPresent()) {
                List<Post> posts = result.get().getPosts();
                String currentUserName = securityContext.getCallerPrincipal().getName();
                boolean isAdmin = securityContext.isCallerInRole(UserRoles.ADMIN);

                System.out.println(isAdmin);

                // Użycie iteratora, aby bezpiecznie usuwać elementy podczas iteracji
                posts.removeIf(post -> !post.getUser().getName().equals(currentUserName) && !isAdmin);

                result.get().setPosts(posts);
            }

            return result;
        }

        /**
         * @return all available categories
         */
        @RolesAllowed({UserRoles.ADMIN,UserRoles.USER})
        public List<Category> findAll() {
            return repository.findAll();
        }

        /**
         * Stores new category in the data store.
         *
         * @param category new category to be saved
         */
        @RolesAllowed(UserRoles.ADMIN)
        public void create(Category category) {
            repository.create(category);
        }

        @RolesAllowed(UserRoles.ADMIN)
        public void delete(UUID id) {
            Category tmp = repository.find(id).orElseThrow(NotFoundException::new);
            repository.delete(tmp);
        }

        @RolesAllowed(UserRoles.ADMIN)
        public void update(Category category) {
            repository.update(category);
        }

}

