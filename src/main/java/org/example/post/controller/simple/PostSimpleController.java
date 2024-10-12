package org.example.post.controller.simple;

import org.example.component.DtoFunctionFactory;
import org.example.post.controller.api.PostController;
import org.example.post.dto.GetPostResponse;
import org.example.post.dto.GetPostsResponse;
import org.example.post.dto.PatchPostRequest;
import org.example.post.dto.PutPostRequest;
import org.example.post.service.PostService;
import org.example.controller.servlet.exception.*;

import java.io.InputStream;
import java.util.UUID;

public class PostSimpleController implements PostController {

    /**
     * Post service.
     */
    private final PostService service;

    /**
     * Factory producing functions for conversion between DTO and entities.
     */
    private final DtoFunctionFactory factory;

    /**
     * @param service post service
     * @param factory factory producing functions for conversion between DTO and entities
     */
    public PostSimpleController(PostService service, DtoFunctionFactory factory) {
        this.service = service;
        this.factory = factory;
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

    @Override
    public GetPostsResponse getUserPosts(UUID id) {
        return service.findAllByUser(id)
                .map(factory.postsToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public GetPostResponse getPost(UUID uuid) {
        return service.find(uuid)
                .map(factory.postToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void putPost(UUID id, PutPostRequest request) {
        try {
            service.create(factory.requestToPost().apply(id, request));
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex);
        }
    }

    @Override
    public void patchPost(UUID id, PatchPostRequest request) {
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

//    @Override //FIXME: Portrait
//    public byte[] getPostPortrait(UUID id) {
//        return service.find(id)
//                .map(Post::getPortrait)
//                .orElseThrow(NotFoundException::new);
//    }
//
//    @Override
//    public void putPostPortrait(UUID id, InputStream portrait) {
//        service.find(id).ifPresentOrElse(
//                entity -> service.updatePortrait(id, portrait),
//                () -> {
//                    throw new NotFoundException();
//                }
//        );
//    }

}
