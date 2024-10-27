package org.example.post.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PostEditModel {
    private String content;
    private int amountOfLikes;
    private CategoryModel category;
}
