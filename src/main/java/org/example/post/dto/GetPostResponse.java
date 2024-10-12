package org.example.post.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.post.entity.Category;
import org.example.user.entity.User;

import java.util.UUID;


/**
 * GET character response. It contains all field that can be presented (but not necessarily changed) to the used.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetPostResponse {

    /**
     * Represents single category.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class Category {
        private UUID id;
        private String name;
        private int positionInHierarchy;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class User {
        private UUID id;
        private String name;
    }

    private UUID id;
    private String content;
    private int amountOfLikes;
    private Category category;
    private User user;
}
