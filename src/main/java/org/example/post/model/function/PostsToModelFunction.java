package org.example.post.model.function;

import org.example.post.entity.Post;
import org.example.post.model.PostsModel;

import java.util.List;
import java.util.function.Function;

public class PostsToModelFunction implements Function<List<Post>, PostsModel> {

    @Override
    public PostsModel apply(List<Post> posts) {
        return PostsModel.builder()
                .posts(posts.stream()
                        .map(post->PostsModel.Post.builder()
                                .id(post.getId())
                                .content(post.getContent())
                                .build())
                        .toList()
                )
                .build();
    }
}
