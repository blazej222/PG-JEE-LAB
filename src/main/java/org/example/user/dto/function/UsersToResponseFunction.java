package org.example.user.dto.function;

import org.example.user.dto.GetUsersResponse;
import org.example.user.entity.User;

import java.util.List;
import java.util.function.Function;

public class UsersToResponseFunction implements Function<List<User>, GetUsersResponse> {

    @Override
    public GetUsersResponse apply(List<User> users) {
        return GetUsersResponse.builder()
                .users(users.stream()
                        .map(user-> GetUsersResponse.User.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .build())
                        .toList())
                .build();
    }
}
