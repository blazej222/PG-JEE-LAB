package org.example.post.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PostsModel {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class Post {

        private UUID id;
        private String content;
        private Long version;
        private LocalDateTime creationDateTime;
        private LocalDateTime modifiedDateTime;
    }


    @Singular
    private List<Post> posts;

}
