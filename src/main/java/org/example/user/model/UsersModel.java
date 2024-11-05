package org.example.user.model;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UsersModel {
    /**
     * Represents single user in list.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class User {

        /**
         * Unique id identifying character.
         */
        private UUID id;

        /**
         * Name of the character.
         */
        private String name;

    }

    /**
     * List of users.
     */
    @Singular
    private List<User> users;

}
