package org.example.post.model.function;

import org.example.post.entity.Post;
import org.example.post.model.PostModel;

import java.io.Serializable;
import java.util.function.Function;

public class PostToModelFunction implements Function<Post, PostModel>, Serializable {

    @Override
    public PostModel apply(Post post) {
        return PostModel.builder()
                .content(post.getContent())
                .amountOfLikes(post.getAmountOfLikes())
                .User(post.getUser().getName())
                .Category(post.getCategory().getName())
                .build();
    }
}
