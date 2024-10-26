package org.example.post.model;

import lombok.*;
import org.example.post.dto.GetPostResponse;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PostModel {
    private UUID id;
    private String content;
    private int amountOfLikes;
    private String Category;
    private String User;
}
