package org.example.post.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.example.category.entity.Category;
import org.example.entity.VersionAndCreationDateAuditable;
import org.example.user.entity.User;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString()
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="posts")
public class Post extends VersionAndCreationDateAuditable implements Serializable {
    @Id
    private UUID id;
    private String content;
    private int amountOfLikes;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Override
    public void updateCreationDateTime()
    {
        super.updateCreationDateTime();
    }

    @Override
    public void updateEditDateTime()
    {
        super.updateEditDateTime();
    }
}
