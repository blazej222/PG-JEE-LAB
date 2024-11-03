package org.example.post.model.function;

import lombok.SneakyThrows;
import org.example.category.entity.Category;
import org.example.post.entity.Post;
import org.example.post.model.PostCreateModel;

import java.util.function.Function;

public class ModelToPostFunction implements Function<PostCreateModel, Post> {
    @Override
    @SneakyThrows
    public Post apply(PostCreateModel model) {
        return Post.builder()
                .id(model.getId())
                .content(model.getContent())
                .amountOfLikes(model.getAmountOfLikes())
                .category(Category.builder()
                        .id(model.getCategory().getId())
                        .build())
                .build();
    }
}
