package org.example.post.dto.function;

import org.example.post.dto.PatchPostRequest;
import org.example.post.entity.Post;

import java.util.function.BiFunction;

public class UpdatePostWithRequestFunction implements BiFunction<Post, PatchPostRequest, Post> {

    @Override
    public Post apply(Post post, PatchPostRequest patchPostRequest) {
        return Post.builder()
                .id(post.getId())
                .content(patchPostRequest.getContent())
                .amountOfLikes(patchPostRequest.getAmountOfLikes())
                .category(post.getCategory())
                .user(post.getUser())
                .build();
    }
}
