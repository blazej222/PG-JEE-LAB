package org.example.user.controller.api;

import org.example.user.dto.GetUserResponse;
import org.example.user.dto.GetUsersResponse;
import org.example.user.dto.PatchUserRequest;
import org.example.user.dto.PutUserRequest;

import java.io.InputStream;
import java.util.UUID;

public interface UserController {

    GetUserResponse getUser(UUID id);

    GetUsersResponse getUsers();

    void putUser(UUID id, PutUserRequest request);

    void patchUser(UUID id, PatchUserRequest request);

    void deleteUser(UUID id);

    byte[] getUserAvatar(UUID id, String pathToAvatars);

    void putUserAvatar(UUID id, InputStream avatar, String pathToAvatars);

    void deleteUserAvatar(UUID id, String pathToAvatars);

    void patchUserAvatar(UUID id, InputStream avatar, String pathToAvatars);
}
