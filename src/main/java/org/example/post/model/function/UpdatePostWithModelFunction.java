package org.example.post.model.function;

import lombok.SneakyThrows;
import org.example.post.entity.Post;
import org.example.post.model.PostEditModel;

import java.io.Serializable;
import java.util.function.BiFunction;

public class UpdatePostWithModelFunction implements BiFunction<Post, PostEditModel, Post>, Serializable {

    @Override
    @SneakyThrows
    public Post apply(Post post, PostEditModel postEditModel) {
        return Post.builder()
                .id(post.getId())
                .content(post.getContent())
                .amountOfLikes(post.getAmountOfLikes())
                .category(post.getCategory())
                .build();
    }
}