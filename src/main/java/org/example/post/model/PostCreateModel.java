package org.example.post.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PostCreateModel {
        private UUID id;
        private String content;
        private int amountOfLikes;
        private CategoryModel category;
        private UUID user;
}
