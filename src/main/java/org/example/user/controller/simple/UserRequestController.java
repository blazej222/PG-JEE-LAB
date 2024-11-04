package org.example.user.controller.simple;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import lombok.NoArgsConstructor;
import org.example.component.DtoFunctionFactory;
import org.example.user.controller.api.UserController;
import org.example.user.dto.GetUserResponse;
import org.example.user.dto.GetUsersResponse;
import org.example.user.dto.PatchUserRequest;
import org.example.user.dto.PutUserRequest;
import org.example.user.services.UserService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@jakarta.ws.rs.Path("")
public class UserRequestController implements UserController {

    private final UserService userService;

    private final DtoFunctionFactory factory;

    private final UriInfo uriInfo;

    private HttpServletResponse response;

    @Context
    public void setResponse(final HttpServletResponse response) {
        this.response = response;
    }

    @Inject
    public UserRequestController(DtoFunctionFactory factory, UserService userService,@SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo) {
        this.factory = factory;
        this.userService = userService;
        this.uriInfo = uriInfo;
    }

    @Override
    public GetUserResponse getUser(UUID id) {
        return userService.find(id)
                .map(factory.userToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public GetUsersResponse getUsers() {
        return factory.usersToResponse().apply(userService.findAll());
    }

    @Override
    public void putUser(UUID id, PutUserRequest request) {
        try {
            userService.create(factory.requestToUser().apply(id, request));
            response.setHeader("Location", uriInfo.getBaseUriBuilder()
                    .path(UserController.class,"getUser")
                    .build(id)
                    .toString());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex);
        }

    }

    @Override
    public void patchUser(UUID id, PatchUserRequest request) {
        userService.find(id).ifPresentOrElse(entity -> userService.update(factory.updateUser().apply(entity, request)), () -> {
            throw new NotFoundException();
        });

    }

    @Override
    public void deleteUser(UUID id) {
        userService.find(id).ifPresentOrElse(
                entity -> userService.delete(id),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public byte[] getUserAvatar(UUID id, String pathToAvatars) {
        Path pathToAvatar = Paths.get(
                pathToAvatars,
                userService.find(id)
                        .map(user -> user.getId().toString())
                        .orElseThrow(() -> new NotFoundException("User does not exist"))
                        + ".png"
        );
        try {
            if (!Files.exists(pathToAvatar)) {
                throw new NotFoundException("User avatar does not exist");
            }
            return Files.readAllBytes(pathToAvatar);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void putUserAvatar(UUID id, InputStream avatar, String pathToAvatars) {
        userService.find(id).ifPresentOrElse(
                user -> {
                    userService.createAvatar(id, avatar, pathToAvatars);
                },
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public void deleteUserAvatar(UUID id, String pathToAvatars) {
        userService.find(id).ifPresentOrElse(
                user -> {
                    try {
                        Path avatarPath = Paths.get(pathToAvatars, user.getId().toString() + ".png");
                        if (!Files.exists(avatarPath)) {
                            throw new NotFoundException("User avatar does not exist");
                        }
                        Files.delete(avatarPath);
                    } catch (IOException e) {
                        throw new NotFoundException(e);
                    }
                },
                () -> {
                    throw new NotFoundException("User does not exist");
                }
        );
    }

    @Override
    public void patchUserAvatar(UUID id, InputStream avatar, String pathToAvatars) {
        userService.find(id).ifPresentOrElse(
                user -> userService.updateAvatar(id, avatar, pathToAvatars),
                () -> {
                    throw new NotFoundException("User does not exist");
                }
        );
    }


}
