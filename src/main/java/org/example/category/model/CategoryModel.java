package org.example.category.model;

import lombok.*;
import org.example.post.entity.Post;

import java.util.List;
import java.util.UUID;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CategoryModel {
        private UUID id;
        private String name;
        private int positionInHierarchy;
        private List<Post> posts;
}
