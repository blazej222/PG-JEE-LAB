package org.example.user.dto;

import lombok.*;
import org.example.post.entity.Post;
import org.example.user.entity.UserRoles;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetUserResponse {

    private UUID id;
    private String name;
    private LocalDate birthday;
    private UserRoles role;

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
    }

    @Singular
    private List<Post> posts;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] avatar;

}
