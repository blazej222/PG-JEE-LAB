package org.example.post.model;

import lombok.*;
import org.example.category.model.CategoryModel;
import org.example.user.model.UserModel;

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
    private UserModel user;
    private Long version;
}
