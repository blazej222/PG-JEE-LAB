package org.example.category.controller.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.example.category.dto.GetCategoriesResponse;
import org.example.category.dto.GetCategoryResponse;
import org.example.category.dto.PatchCategoryRequest;
import org.example.category.dto.PutCategoryRequest;

import java.util.UUID;

@Path("")
public interface CategoryController {
    @GET
    @Path("/categories")
    @Produces(MediaType.APPLICATION_JSON)
    GetCategoriesResponse getCategories();

    @GET
    @Path("/categories/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    GetCategoryResponse getCategory(@PathParam("id") UUID id);

    @PUT
    @Path("/categories/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void putCategory(@PathParam("id") UUID id, PutCategoryRequest putCategoryRequest);

    @PATCH
    @Path("/categories/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void patchCategory(@PathParam("id") UUID id, PatchCategoryRequest patchCategoryRequest);

    /**
     * @param id profession's id
     */
    @DELETE
    @Path("/categories/{id}")
    void deleteCategory(@PathParam("id") UUID id);

}
