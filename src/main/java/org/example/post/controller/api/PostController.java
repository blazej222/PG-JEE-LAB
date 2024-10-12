package org.example.post.controller.api;

import org.example.post.dto.GetPostResponse;
import org.example.post.dto.GetPostsResponse;
import org.example.post.dto.PatchPostRequest;
import org.example.post.dto.PutPostRequest;

import java.util.UUID;

public interface PostController {
    GetPostsResponse getPosts();
    GetPostsResponse getCategoryPosts(UUID id);
    GetPostsResponse getUserPosts(UUID id);
    GetPostResponse getPost(UUID id);
    void putPost(UUID id, PutPostRequest postRequest);
    void patchPost(UUID id, PatchPostRequest postRequest);
    void deletePost(UUID id);

//    byte[] getCharacterPortrait(UUID id);
//    void putCharacterPortrait(UUID id, InputStream portrait);
    //FIXME: Avatar

}
