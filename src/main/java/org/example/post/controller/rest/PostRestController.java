package org.example.post.controller.rest;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.example.component.DtoFunctionFactory;
import org.example.post.controller.api.PostController;
import org.example.post.dto.GetPostResponse;
import org.example.post.dto.GetPostsResponse;
import org.example.post.dto.PatchPostRequest;
import org.example.post.dto.PutPostRequest;
import org.example.post.service.PostService;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.BadRequestException;

import java.util.UUID;

@Path("")
public class PostRestController implements PostController {

    /**
     * Post service.
     */
    private PostService service;

    /**
     * Factory producing functions for conversion between DTO and entities.
     */
    private final DtoFunctionFactory factory;

    private final UriInfo uriInfo;

    private HttpServletResponse response;

    @Context
    public void setResponse(final HttpServletResponse response) {
        this.response = response;
    }

    /**
     * @param service post service
     * @param factory factory producing functions for conversion between DTO and entities
     */
    @Inject
    public PostRestController(DtoFunctionFactory factory, @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo) {
        this.factory = factory;
        this.uriInfo = uriInfo;
    }

    @Override
    public GetPostsResponse getPosts() {
        return factory.postsToResponse().apply(service.findAll());
    }

    @Override
    public GetPostsResponse getCategoryPosts(UUID id) {
        return service.findAllByCategory(id)
                .map(factory.postsToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @EJB
    public void setService(PostService service) {
        this.service = service;
    }

    @Override
    public GetPostsResponse getUserPosts(UUID id) {
        return service.findAllByUser(id)
                .map(factory.postsToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public GetPostResponse getPost(UUID id) {
        return service.find(id)
                .map(factory.postToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    @SneakyThrows
    public void putPost(UUID id, PutPostRequest request,UUID catid) {
        try {
            request.setCategory(catid);
            service.create(factory.requestToPost().apply(id, request));
            throw new WebApplicationException(Response.Status.CREATED);
        } catch (EJBException ex) {
            throw new BadRequestException(ex);
        }
    }

    @Override
    public void patchPost(UUID id, PatchPostRequest request, UUID catid) {
        request.setCategory(catid);
        service.find(id).ifPresentOrElse(
                entity -> service.update(factory.updatePost().apply(entity, request)),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public void deletePost(UUID id) {
        service.find(id).ifPresentOrElse(
                entity -> service.delete(id),
                () -> {
                    throw new NotFoundException();
                }
        );
    }
}
