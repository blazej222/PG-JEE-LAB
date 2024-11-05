package org.example.post.dto.function;

import org.example.post.dto.PutPostRequest;
import org.example.category.entity.Category;
import org.example.post.entity.Post;
import org.example.user.entity.User;

import java.util.UUID;
import java.util.function.BiFunction;

public class RequestToPostFunction implements BiFunction<UUID, PutPostRequest, Post> {

    @Override
    public Post apply(UUID uuid, PutPostRequest request) {
        return Post.builder()
                .id(uuid)
                .content(request.getContent())
                .amountOfLikes(request.getAmountOfLikes())
                .category(Category.builder()
                        .id(request.getCategory()
                        )
                        .build())
                .user(User.builder()
                        .id(request.getUser()).
                        build())
                .build();
    }
}
