package org.example.post.model.function;

import org.example.post.entity.Post;
import org.example.post.model.PostEditModel;

import java.util.function.Function;

public class PostToEditModelFunction implements Function<Post, PostEditModel> {

    @Override
    public PostEditModel apply(Post post) {
        return PostEditModel.builder()
                .content(post.getContent())
                .amountOfLikes(post.getAmountOfLikes())
                .build();
    }
}
