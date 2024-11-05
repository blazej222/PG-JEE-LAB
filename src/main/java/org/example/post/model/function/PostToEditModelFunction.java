package org.example.post.model.function;

import org.example.post.entity.Post;
import org.example.post.model.PostEditModel;
import org.example.user.model.function.UserToModelFunction;

import java.util.function.Function;

public class PostToEditModelFunction implements Function<Post, PostEditModel> {

    private final UserToModelFunction userToModelFunction;

    public PostToEditModelFunction(UserToModelFunction userToModelFunction) {
        this.userToModelFunction = userToModelFunction;
    }

    @Override
    public PostEditModel apply(Post post) {
        return PostEditModel.builder()
                .content(post.getContent())
                .amountOfLikes(post.getAmountOfLikes())
                .user(userToModelFunction.apply(post.getUser()))
                .build();
    }
}
