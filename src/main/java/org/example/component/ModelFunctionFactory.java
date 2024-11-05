package org.example.component;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.category.model.function.CategoriesToModelFunction;
import org.example.category.model.function.CategoryToModelFunction;
import org.example.post.model.function.*;
import org.example.user.model.function.UserToModelFunction;
import org.example.user.model.function.UsersToModelFunction;

@ApplicationScoped
public class ModelFunctionFactory {

    public PostToModelFunction postToModel(){
        return new PostToModelFunction();
    }

    public PostsToModelFunction postsToModel(){
        return new PostsToModelFunction();
    }

    public PostToEditModelFunction postToEditModel(){
        return new PostToEditModelFunction(userToModel());
    }

    public ModelToPostFunction modelToPost(){
        return new ModelToPostFunction();
    }

    public CategoryToModelFunction categoryToModel(){
        return new CategoryToModelFunction();
    }

    public CategoriesToModelFunction categoriesToModel(){
        return new CategoriesToModelFunction();
    }

    public UpdatePostWithModelFunction updatePost(){
        return new UpdatePostWithModelFunction();
    }

    /**
     * Returns a function to convert a single {@link User} to {@link UserModel}.
     *
     * @return UserToModelFunction instance
     */
    public UserToModelFunction userToModel() {
        return new UserToModelFunction();
    }

    /**
     * Returns a function to convert a list {@link Character} to {@link UsersModel}.
     *
     * @return UserToModelFunction instance
     */
    public UsersToModelFunction usersToModel() {
        return new UsersToModelFunction();
    }

}
