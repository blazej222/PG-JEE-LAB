package org.example.configuration.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.datastore.component.DataStore;
import org.example.post.repository.api.CategoryRepository;
import org.example.post.repository.api.PostRepository;
import org.example.post.repository.memory.CategoryInMemoryRepository;
import org.example.post.repository.memory.PostInMemoryRepository;
import org.example.post.service.CategoryService;
import org.example.post.service.PostService;
import org.example.user.repository.api.UserRepository;
import org.example.user.repository.memory.UserInMemoryRepository;
import org.example.user.services.UserService;

/**
 * Listener started automatically on servlet context initialized. Creates an instance of services (business layer) and
 * puts them in the application (servlet) context.
 */
@WebListener//using annotation does not allow configuring order
public class CreateServices implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        DataStore dataSource = (DataStore) event.getServletContext().getAttribute("datasource");

        UserRepository userRepository = new UserInMemoryRepository(dataSource);
        CategoryRepository categoryRepository = new CategoryInMemoryRepository(dataSource);
        PostRepository postRepository = new PostInMemoryRepository(dataSource);

        event.getServletContext().setAttribute("userService", new UserService(userRepository));
        event.getServletContext().setAttribute("postService", new PostService(postRepository, categoryRepository, userRepository));
        event.getServletContext().setAttribute("categoryService", new CategoryService(categoryRepository));
    }

}