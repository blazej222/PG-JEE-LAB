package org.example.component;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.category.model.function.CategoriesToModelFunction;
import org.example.category.model.function.CategoryToModelFunction;
import org.example.post.model.function.*;

@ApplicationScoped
public class ModelFunctionFactory {

    public PostToModelFunction postToModel(){
        return new PostToModelFunction();
    }

    public PostsToModelFunction postsToModel(){
        return new PostsToModelFunction();
    }

    public PostToEditModelFunction postToEditModel(){
        return new PostToEditModelFunction();
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
}
