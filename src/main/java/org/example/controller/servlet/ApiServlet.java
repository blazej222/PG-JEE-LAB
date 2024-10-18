package org.example.controller.servlet;

import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.controller.servlet.exception.AlreadyExistsException;
import org.example.controller.servlet.exception.NotFoundException;
import org.example.post.controller.api.CategoryController;
import org.example.post.controller.api.PostController;
import org.example.post.dto.PatchPostRequest;
import org.example.post.dto.PutPostRequest;
import org.example.user.controller.api.UserController;
import org.example.user.dto.PutUserRequest;
import org.example.user.dto.PatchUserRequest;


import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@WebServlet(urlPatterns = {
        ApiServlet.Paths.API + "/*"
})
@MultipartConfig(maxFileSize = 200 * 1024)
public class ApiServlet extends HttpServlet {

    /**
     * Controller for managing collections posts' representations.
     */
    private final PostController postController;

    /**
     * Controller for managing collections categories' representations.
     */
    private final CategoryController categoryController;

    private final UserController userController;

    private String avatarPath;

    /**
     * Definition of paths supported by this servlet. Separate inner class provides composition for static fields.
     */
    public static final class Paths {

        /**
         * All API operations. Version v1 will be used to distinguish from other implementations.
         */
        public static final String API = "/api";

    }

    /**
     * Patterns used for checking servlet path.
     */
    public static final class Patterns {

        /**
         * UUID
         */
        private static final Pattern UUID = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

        /**
         * All posts.
         */
        public static final Pattern POSTS = Pattern.compile("/posts/?");

        /**
         * Single post.
         */
        public static final Pattern POST = Pattern.compile("/posts/(%s)".formatted(UUID.pattern()));

        /**
         * All categories.
         */
        public static final Pattern CATEGORIES = Pattern.compile("/categories/?");

        /**
         * All posts of single category.
         */
        public static final Pattern CATEGORY_POSTS = Pattern.compile("/categories/(%s)/posts/?".formatted(UUID.pattern()));

        /**
         * All posts of single user.
         */
        public static final Pattern USER_POSTS = Pattern.compile("/users/(%s)/posts/?".formatted(UUID.pattern()));

        public static final Pattern USER = Pattern.compile("/users/(%s)".formatted(UUID.pattern()));

        public static final Pattern USER_AVATAR = Pattern.compile("/users/(%s)/avatar".formatted(UUID.pattern()));

        public static final Pattern USERS = Pattern.compile("/users/?");

    }

    /**
     * JSON-B mapping object. According to open liberty documentation creating this is expensive. The JSON-B is only one
     * of many solutions. JSON strings can be built by hand {@link StringBuilder} or with JSON-P API. Both JSON-B and
     * JSON-P are part of Jakarta EE whereas JSON-B is newer standard.
     */
    private final Jsonb jsonb = JsonbBuilder.create();

    @Inject
    public ApiServlet(PostController postController, CategoryController categoryController, UserController userController) {
        this.postController = postController;
        this.categoryController = categoryController;
        this.userController = userController;
        this.avatarPath = "F:\\Politechnika\\Sem7\\JEE\\JEE-Lab\\avatars";
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equals("PATCH")) {
            doPatch(request, response);
        } else {
            super.service(request, response);
        }
    }

    @SuppressWarnings("RedundantThrows")
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.API.equals(servletPath)) {
            if (path.matches(Patterns.POSTS.pattern())) {
                response.setContentType("application/json");
                response.getWriter().write(jsonb.toJson(postController.getPosts()));
                return;
            } else if (path.matches(Patterns.POST.pattern())) {
                response.setContentType("application/json");
                UUID uuid = extractUuid(Patterns.POST, path);
                response.getWriter().write(jsonb.toJson(postController.getPost(uuid)));
                return;
            } else if (path.matches(Patterns.CATEGORIES.pattern())) {
                response.setContentType("application/json");
                response.getWriter().write(jsonb.toJson(categoryController.getCategories()));
                return;
            } else if (path.matches(Patterns.CATEGORY_POSTS.pattern())) {
                response.setContentType("application/json");
                UUID uuid = extractUuid(Patterns.CATEGORY_POSTS, path);
                response.getWriter().write(jsonb.toJson(postController.getCategoryPosts(uuid)));
                return;
            } else if (path.matches(Patterns.USER_POSTS.pattern())) {
                response.setContentType("application/json");
                UUID uuid = extractUuid(Patterns.USER_POSTS, path);
                response.getWriter().write(jsonb.toJson(postController.getUserPosts(uuid)));
                return;
            } else if (path.matches(Patterns.USER.pattern())) {
                response.setContentType("application/json");
                UUID uuid = extractUuid(Patterns.USER, path);
                try {
                    response.getWriter().write(jsonb.toJson(userController.getUser(uuid)));
                } catch (NotFoundException e) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                return;
            } else if (path.matches(Patterns.USER_AVATAR.pattern())) {
                response.setContentType("image/png");
                UUID uuid = extractUuid(Patterns.USER_AVATAR, path);
                try {
                    byte avatar[] = userController.getUserAvatar(uuid, avatarPath);
                    response.setContentLength(avatar.length);
                    response.getOutputStream().write(avatar);
                } catch (NotFoundException e) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
                }
                return;
            } else if (path.matches(Patterns.USERS.pattern())) {
                response.setContentType("application/json");
                try {
                    response.getWriter().write(jsonb.toJson(userController.getUsers()));
                } catch (NotFoundException ex) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.API.equals(servletPath)) {
            if (path.matches(Patterns.POST.pattern())) {
                UUID uuid = extractUuid(Patterns.POST, path);
                postController.putPost(uuid, jsonb.fromJson(request.getReader(), PutPostRequest.class));
                response.addHeader("Location", createUrl(request, Paths.API, "posts", uuid.toString()));
                return;
            } else if(path.matches(Patterns.USER.pattern())) {
                UUID uuid = extractUuid(Patterns.USER, path);
                userController.putUser(uuid, jsonb.fromJson(request.getReader(), PutUserRequest.class));
                response.addHeader("Location", createUrl(request, Paths.API, "users", uuid.toString()));
                response.setStatus(HttpServletResponse.SC_CREATED);
                return;
            } else if(path.matches(Patterns.USER_AVATAR.pattern())) {
                UUID uuid = extractUuid(Patterns.USER_AVATAR, path);
                response.setContentType("image/png");
                try {
                    userController.putUserAvatar(uuid, request.getPart("avatar").getInputStream(), avatarPath);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } catch (AlreadyExistsException ex) {
                    response.sendError(HttpServletResponse.SC_CONFLICT, ex.getMessage());
                } catch (NotFoundException ex) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
                }
                return;
            }
//            else if (path.matches(Patterns.POST_PORTRAIT.pattern())) {
//                UUID uuid = extractUuid(Patterns.POST_PORTRAIT, path);
//                postController.putPostPortrait(uuid, request.getPart("portrait").getInputStream());
//                return;
//            } //FIXME: Avatar
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @SuppressWarnings("RedundantThrows")
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.API.equals(servletPath)) {
            if (path.matches(Patterns.POST.pattern())) {
                UUID uuid = extractUuid(Patterns.POST, path);
                postController.deletePost(uuid);
                return;
            }
            else if (path.matches(Patterns.USER.pattern())) {
                UUID uuid = extractUuid(Patterns.USER, path);
                try {
                    userController.deleteUser(uuid);
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } catch (NotFoundException ex) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                return;
            } else if (path.matches(Patterns.USER_AVATAR.pattern())) {
                UUID uuid = extractUuid(Patterns.USER_AVATAR, path);
                try {
                    userController.deleteUserAvatar(uuid, avatarPath);
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } catch (NotFoundException ex) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
                }
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    /**
     * Called by the server (via the <code>service</code> method) to allow a servlet to handle a PATCH request.
     *
     * @param request  {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param response {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws ServletException if the request for the PATCH cannot be handled
     * @throws IOException      if an input or output error occurs while the servlet is handling the PATCH request
     */
    @SuppressWarnings("RedundantThrows")
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.API.equals(servletPath)) {
            if (path.matches(Patterns.POST.pattern())) {
                UUID uuid = extractUuid(Patterns.POST, path);
                postController.patchPost(uuid, jsonb.fromJson(request.getReader(), PatchPostRequest.class));
                return;
            }
            else if (path.matches(Patterns.USER.pattern())) {
                UUID uuid = extractUuid(Patterns.USER, path);
                try {
                    userController.patchUser(uuid, jsonb.fromJson(request.getReader(), PatchUserRequest.class));
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } catch (NotFoundException ex) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                return;
            } else if (path.matches(Patterns.USER_AVATAR.pattern())) {
                response.setContentType("image/png");
                UUID uuid = extractUuid(Patterns.USER_AVATAR, path);
                try {
                    userController.patchUserAvatar(uuid, request.getPart("avatar").getInputStream(), avatarPath);
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } catch (NotFoundException ex) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
                }
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    /**
     * Extracts UUID from path using provided pattern. Pattern needs to contain UUID in first regular expression group.
     *
     * @param pattern regular expression pattern with
     * @param path    request path containing UUID
     * @return extracted UUID
     */
    private static UUID extractUuid(Pattern pattern, String path) {
        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            return UUID.fromString(matcher.group(1));
        }
        throw new IllegalArgumentException("No UUID in path.");
    }

    /**
     * Gets path info from the request and returns it. No null is returned, instead empty string is used.
     *
     * @param request original servlet request
     * @return path info (not null)
     */
    private String parseRequestPath(HttpServletRequest request) {
        String path = request.getPathInfo();
        path = path != null ? path : "";
        return path;
    }

    /**
     * Creates URL using host, port and context root from servlet request and any number of path elements. If any of
     * path elements starts or ends with '/' post, that post is removed.
     *
     * @param request servlet request
     * @param paths   any (can be none) number of path elements
     * @return created url
     */
    public static String createUrl(HttpServletRequest request, String... paths) {
        StringBuilder builder = new StringBuilder();
        builder.append(request.getScheme())
                .append("://")
                .append(request.getServerName())
                .append(":")
                .append(request.getServerPort())
                .append(request.getContextPath());
        for (String path : paths) {
            builder.append("/")
                    .append(path, path.startsWith("/") ? 1 : 0, path.endsWith("/") ? path.length() - 1 : path.length());
        }
        return builder.toString();
    }
}
