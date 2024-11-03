package org.example.post.controller.api;

import jakarta.enterprise.context.Dependent;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.example.post.dto.GetPostResponse;
import org.example.post.dto.GetPostsResponse;
import org.example.post.dto.PatchPostRequest;
import org.example.post.dto.PutPostRequest;

import java.util.UUID;

@Path("")
public interface PostController {
    @GET
    @Path("/posts")
    @Produces(MediaType.APPLICATION_JSON)
    GetPostsResponse getPosts();

    @GET
    @Path("/categories/{id}/posts")
    @Produces(MediaType.APPLICATION_JSON)
    GetPostsResponse getCategoryPosts(@PathParam("id") UUID id);

    @GET
    @Path("/users/{id}/posts")
    @Produces(MediaType.APPLICATION_JSON)
    GetPostsResponse getUserPosts(@PathParam("id")UUID id);

    @GET
    @Path("/posts/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    GetPostResponse getPost(@PathParam("id")UUID id);

    @PUT
    @Path("/posts/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void putPost(@PathParam("id") UUID id, PutPostRequest postRequest);

    @PATCH
    @Path("/posts/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void patchPost(@PathParam("id") UUID id, PatchPostRequest postRequest);

    @DELETE
    @Path("/posts/{id}")
    void deletePost(@PathParam("id")UUID id);

}
