package org.example.category.controller.rest;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.example.category.dto.PatchCategoryRequest;
import org.example.category.dto.PutCategoryRequest;
import org.example.component.DtoFunctionFactory;
import org.example.category.controller.api.CategoryController;
import org.example.category.dto.GetCategoriesResponse;
import org.example.category.dto.GetCategoryResponse;
import org.example.category.service.CategoryService;
import jakarta.ws.rs.NotFoundException;
import org.example.post.controller.api.PostController;

import java.util.UUID;

@Path("")
public class CategoryRestController implements CategoryController {
    private CategoryService service;
    private final DtoFunctionFactory factory;

    @Inject
    public CategoryRestController( DtoFunctionFactory factory) {
        this.factory = factory;
    }

    @EJB
    public void setService(CategoryService service) {
        this.service = service;
    }

    @Override
    public GetCategoriesResponse getCategories() {
        return factory.categoriesToResponse().apply(service.findAll());
    }

    @Override
    public GetCategoryResponse getCategory(UUID id) {
        return service.find(id)
                .map(factory.categoryToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void putCategory(UUID id, PutCategoryRequest request) {
        try{
            service.create(factory.requestToCategory().apply(id,request));
            throw new WebApplicationException(Response.Status.CREATED);
        }catch (IllegalArgumentException ex){
            throw new BadRequestException(ex);
        }
    }

    @Override
    public void patchCategory(UUID id, PatchCategoryRequest request) {
        service.find(id).ifPresentOrElse(
                entity -> service.update(factory.updateCategory().apply(entity, request)),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public void deleteCategory(UUID id) {
        service.find(id).ifPresentOrElse(
                entity -> service.delete(id),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

}
