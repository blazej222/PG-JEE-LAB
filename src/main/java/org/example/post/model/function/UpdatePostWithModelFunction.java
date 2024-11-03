package org.example.post.model.function;

import lombok.SneakyThrows;
import org.example.category.entity.Category;
import org.example.post.entity.Post;
import org.example.post.model.PostEditModel;

import java.io.Serializable;
import java.util.function.BiFunction;

public class UpdatePostWithModelFunction implements BiFunction<Post, PostEditModel, Post>, Serializable {

    @Override
    @SneakyThrows
    public Post apply(Post post, PostEditModel request) {
        return Post.builder()
                .id(post.getId())
                .content(request.getContent())
                .amountOfLikes(request.getAmountOfLikes())
                .category(Category.builder()
                        .id(request.getCategory().getId())
                        .name(request.getCategory().getName())
                        .posts(request.getCategory().getPosts())
                        .positionInHierarchy(request.getCategory().getPositionInHierarchy())
                        .build()
                ).build();
    }
}
