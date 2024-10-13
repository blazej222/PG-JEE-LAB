package org.example.component;

import org.example.post.dto.function.*;
import org.example.post.entity.*;
import org.example.post.dto.*;
import org.example.user.dto.function.RequestToUserFunction;
import org.example.user.dto.function.UpdateUserWithRequestFunction;
import org.example.user.dto.function.UserToResponseFunction;
import org.example.user.dto.function.UsersToResponseFunction;
import org.example.user.entity.User;
import org.example.user.dto.*;

public class DtoFunctionFactory {
    /**
     * Returns a function to convert a single {@link Post} to {@link GetPostResponse}.
     *
     * @return PostToResponseFunction instance
     */
    public PostToResponseFunction postToResponse() {
        return new PostToResponseFunction();
    }

    /**
     * Returns a function to convert a list of {@link Post} to {@link GetPostsResponse}.
     *
     * @return PostsToResponseFunction instance
     */
    public PostsToResponseFunction postsToResponse() {
        return new PostsToResponseFunction();
    }

    /**
     * Returns a function to convert a single {@link Category} to {@link GetCategoryResponse}.
     *
     * @return CategoryToResponseFunction instance
     */
    public CategoryToResponseFunction categoryToResponse() {
        return new CategoryToResponseFunction();
    }

    /**
     * Returns a function to convert a list of {@link Category} to {@link GetCategoriesResponse}.
     *
     * @return CategoriesToResponseFunction instance
     */
    public CategoriesToResponseFunction categoriesToResponse() {
        return new CategoriesToResponseFunction();
    }

    /**
     * Returns a function to convert a {@link PutPostRequest} to a {@link Post}.
     *
     * @return RequestToPostFunction instance
     */
    public RequestToPostFunction requestToPost() {
        return new RequestToPostFunction();
    }

    /**
     * Returns a function to update a {@link Post}.
     *
     * @return UpdatePostFunction instance
     */
    public UpdatePostWithRequestFunction updatePost() {
        return new UpdatePostWithRequestFunction();
    }

    /**
     * Returns a function to convert a {@link PutUserRequest} to a {@link User}.
     *
     * @return RequestToUserFunction instance
     */
    public RequestToUserFunction requestToUser() {
        return new RequestToUserFunction();
    }

    /**
     * Returns a function to update a {@link User}.
     *
     * @return UpdateUserFunction instance
     */
    public UpdateUserWithRequestFunction updateUser() {
        return new UpdateUserWithRequestFunction();
    }

    /**
     * Returns a function to convert a single {@link User} to {@link GetUserResponse}.
     *
     * @return UserToResponseFunction instance
     */
    public UserToResponseFunction userToResponse() {
        return new UserToResponseFunction();
    }

    public UsersToResponseFunction usersToResponse() {
        return new UsersToResponseFunction();
    }

}
