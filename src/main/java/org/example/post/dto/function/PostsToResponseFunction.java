package org.example.post.dto.function;

import org.example.post.dto.GetPostsResponse;
import org.example.post.entity.Post;

import java.util.List;
import java.util.function.Function;

public class PostsToResponseFunction implements Function<List<Post>, GetPostsResponse>{
    @Override
    public GetPostsResponse apply(List<Post> posts) {
        return GetPostsResponse.builder()
                .posts(posts.stream()
                        .map(post -> GetPostsResponse.Post.builder()
                                .id(post.getId())
                                        .content(post.getContent())
                                        .build())
                                .toList()
                        )
                .build();
    }
}
