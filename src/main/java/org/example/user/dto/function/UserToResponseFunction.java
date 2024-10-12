package org.example.user.dto.function;

import org.example.user.dto.GetUserResponse;
import org.example.user.entity.User;

import java.util.function.Function;

public class UserToResponseFunction implements Function<User, GetUserResponse> {

    @Override
    public GetUserResponse apply(User user) {
        return GetUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .birthday(user.getBirthday())
                .posts(user.getPosts())
                .role(user.getRole())
                .avatar(user.getAvatar())
                .build();
    }
}
