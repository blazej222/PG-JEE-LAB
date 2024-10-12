package org.example.configuration.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.component.DtoFunctionFactory;
import org.example.post.controller.simple.CategorySimpleController;
import org.example.post.controller.simple.PostSimpleController;
import org.example.post.service.CategoryService;
import org.example.post.service.PostService;

/**
 * Listener started automatically on servlet context initialized. Creates an instance of controllers and puts them in
 * the application (servlet) context.
 */
@WebListener//using annotation does not allow configuring order
public class CreateControllers implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        PostService postService = (PostService) event.getServletContext().getAttribute("postService");
        CategoryService categoryService = (CategoryService) event.getServletContext().getAttribute("categoryService");

        event.getServletContext().setAttribute("postController", new PostSimpleController(
                postService,
                new DtoFunctionFactory()
        ));

        event.getServletContext().setAttribute("categoryController", new CategorySimpleController(
                categoryService,
                new DtoFunctionFactory()
        ));
    }
}
