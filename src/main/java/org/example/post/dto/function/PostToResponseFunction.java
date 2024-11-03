package org.example.post.dto.function;

import org.example.post.dto.GetPostResponse;
import org.example.post.entity.Post;

import java.util.function.Function;

public class PostToResponseFunction implements Function<Post, GetPostResponse> {

    @Override
    public GetPostResponse apply(Post entity) {
        return GetPostResponse.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .amountOfLikes(entity.getAmountOfLikes())
                .category(GetPostResponse.Category.builder()
                        .id(entity.getCategory().getId())
                        .name(entity.getCategory().getName())
                        .build()
                )
//                .user(GetPostResponse.User.builder()
//                        .id(entity.getUser().getId())
//                        .name(entity.getUser().getName())
//                        .build()
//                )
                .build();
    }
}
