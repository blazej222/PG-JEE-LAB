package org.example.user.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.example.post.entity.Post;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode

public class User implements Serializable{
    private UUID id;
    private String name;
    private LocalDate birthday;
    private UserRoles role;
    private List<Post> posts;
}
