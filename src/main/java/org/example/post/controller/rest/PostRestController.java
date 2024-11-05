package org.example.post.controller.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBAccessException;
import jakarta.ejb.EJBException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.example.component.DtoFunctionFactory;
import org.example.post.controller.api.PostController;
import org.example.post.dto.GetPostResponse;
import org.example.post.dto.GetPostsResponse;
import org.example.post.dto.PatchPostRequest;
import org.example.post.dto.PutPostRequest;
import org.example.post.service.PostService;
import org.example.user.entity.UserRoles;

import java.util.UUID;

@Path("")
@Log
@RolesAllowed({UserRoles.USER,UserRoles.ADMIN})
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

    @Inject
    public PostRestController(DtoFunctionFactory factory, @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo) {
        this.factory = factory;
        this.uriInfo = uriInfo;
    }

    @Override
    public GetPostsResponse getPosts() {
        return factory.postsToResponse().apply(service.findAllForCallerPrincipal());
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
        try{
            return service.findForCallerPrincipal(id)
                    .map(factory.postToResponse()).orElse(null);
        }catch(EJBAccessException ex){
            throw new ForbiddenException(ex.getMessage());
        }catch(Exception ex){
            throw new NotFoundException(ex.getMessage());
        }
    }

    @Override
    @SneakyThrows
    public void putPost(UUID id, PutPostRequest request,UUID catid) {
        try {
            request.setCategory(catid);
            //FIXME: Potential missing try catch clause?
            service.createForCallerPrincipal(factory.requestToPost().apply(id, request));
            throw new WebApplicationException(Response.Status.CREATED);
        } catch (EJBException ex) {
            throw new BadRequestException(ex);
        }
    }

    @Override
    public void patchPost(UUID id, PatchPostRequest request, UUID catid) {
        request.setCategory(catid);
        service.find(id).ifPresentOrElse(
                entity -> {
                    try{
                        service.update(factory.updatePost().apply(entity, request));
                    }catch (EJBAccessException ex){
                        throw new ForbiddenException(ex.getMessage());
                    }

                },
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public void deletePost(UUID id) {
        service.find(id).ifPresentOrElse(
                entity -> {
                    try{
                        service.delete(id);
                    }catch (EJBAccessException ex){
                        throw new ForbiddenException(ex.getMessage());
                    }
                },
                () -> {
                    throw new NotFoundException();
                }
        );
    }
}
