package org.example.user.model.function;

import org.example.user.entity.User;
import org.example.user.model.UserModel;

import java.io.Serializable;
import java.util.function.Function;

public class UserToModelFunction implements Function<User, UserModel>, Serializable {

    @Override
    public UserModel apply(User user) {
        return UserModel.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
