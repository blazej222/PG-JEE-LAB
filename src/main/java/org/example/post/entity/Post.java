package org.example.post.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.example.user.entity.User;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode
public class Post {
    private UUID id;
    private String content;
    private int amountOfLikes;
    private User owner;
    private Category category;
}
