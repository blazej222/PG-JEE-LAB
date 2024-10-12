package org.example.user.dto.function;

import org.example.user.dto.PatchUserRequest;
import org.example.user.entity.User;

import java.util.function.BiFunction;

public class UpdateUserWithRequestFunction implements BiFunction<User, PatchUserRequest, User> {

    @Override
    public User apply(User user, PatchUserRequest request) {
        return User.builder()
                .id(user.getId())
                .name(request.getName())
                .avatar(user.getAvatar())
                .birthday(request.getBirthday())
                .role(user.getRole())
                .build();
    }
}
