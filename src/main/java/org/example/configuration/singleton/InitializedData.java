package org.example.configuration.singleton;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.control.RequestContextController;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextListener;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.example.category.entity.Category;
import org.example.post.entity.Post;
import org.example.category.service.CategoryService;
import org.example.post.service.PostService;
import org.example.user.entity.User;
import org.example.user.entity.UserRoles;
import org.example.user.services.UserService;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Listener started automatically on servlet context initialized. Fetches instance of the datasource from the servlet
 * context and fills it with default content. Normally this class would fetch database datasource and init data only in
 * cases of empty database. When using persistence storage application instance should be initialized only during first
 * run in order to init database with starting data. Good place to create first default admin user.
 */
@Singleton
@Startup
@TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
@NoArgsConstructor
public class InitializedData implements ServletContextListener {

    /**
     * Post service.
     */
    private PostService postService;

    /**
     * User service.
     */
    private UserService userService;

    /**
     * Category service.
     */
    private CategoryService categoryservice;

    @EJB
    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    @EJB
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @EJB
    public void setCategoryservice(CategoryService categoryservice) {
        this.categoryservice = categoryservice;
    }

    /**
     * Initializes database with some example values. Should be called after creating this object. This object should be
     * created only once.
     */
    @PostConstruct
    @SneakyThrows
    private void init() {
        if(userService.find("Alice").isEmpty()) {
            User admin = User.builder()
                    .id(UUID.fromString("c4804e0f-769e-4ab9-9ebe-0578fb4f00a6"))
                    .name("admin")
                    .birthday(LocalDate.of(1990, 10, 21))
                    .role(UserRoles.ADMIN)
                    .build();

            User kevin = User.builder()
                    .id(UUID.fromString("81e1c2a9-7f57-439b-b53d-6db88b071e4e"))
                    .name("Kevin")
                    .birthday(LocalDate.of(2001, 1, 16))
                    .role(UserRoles.USER)
                    .build();

            User alice = User.builder()
                    .id(UUID.fromString("ed6cfb2a-cad7-47dd-9b56-9d1e3c7a4197"))
                    .name("Alice")
                    .birthday(LocalDate.of(2002, 3, 19))
                    .role(UserRoles.USER)
                    .build();

            userService.create(admin);
            userService.create(kevin);
            userService.create(alice);


            Category bard = Category.builder()
                    .id(UUID.fromString("f5875513-bf7b-4ae1-b8a5-5b70a1b90e76"))
                    .name("Category 1")
                    .positionInHierarchy(1)
                    .build();

            Category cleric = Category.builder()
                    .id(UUID.fromString("5d1da2ae-6a14-4b6d-8b4f-d117867118d4"))
                    .name("Category 2")
                    .positionInHierarchy(2)
                    .build();

            Category warrior = Category.builder()
                    .id(UUID.fromString("2d9b1e8c-67c5-4188-a911-5f064a63d8cd"))
                    .name("Category 3")
                    .positionInHierarchy(3)
                    .build();

            Category rogue = Category.builder()
                    .id(UUID.randomUUID())
                    .name("Category 4")
                    .positionInHierarchy(4)
                    .build();

            categoryservice.create(bard);
            categoryservice.create(cleric);
            categoryservice.create(warrior);
            categoryservice.create(rogue);

            Post calvian = Post.builder()
                    .id(UUID.fromString("525d3e7b-bb1f-4c13-bf17-926d1a12e4c0"))
                    .content("Calvian")
                    .amountOfLikes(7)
                    .category(bard)
                    .user(kevin)
                    .build();

            Post uhlbrecht = Post.builder()
                    .id(UUID.fromString("cc0b0577-bb6f-45b7-81d6-3db88e6ac19f"))
                    .content("Uhlbrecht")
                    .amountOfLikes(77)
                    .category(cleric)
                    .user(kevin)
                    .build();

            Post eloise = Post.builder()
                    .id(UUID.fromString("f08ef7e3-7f2a-4378-b1fb-2922d730c70d"))
                    .content("Eloise")
                    .amountOfLikes(777)
                    .category(warrior)
                    .user(alice)
                    .build();

            Post zereni = Post.builder()
                    .id(UUID.fromString("ff327e8a-77c0-4f9b-90a2-89e16895d1e1"))
                    .content("Zereni")
                    .amountOfLikes(7777)
                    .category(rogue)
                    .user(alice)
                    .build();

            postService.create(calvian);
            postService.create(uhlbrecht);
            postService.create(eloise);
            postService.create(zereni);

        }
    }

}