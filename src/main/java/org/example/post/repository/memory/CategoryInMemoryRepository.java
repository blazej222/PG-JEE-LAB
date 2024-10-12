package org.example.post.repository.memory;

import org.example.datastore.component.DataStore;
import org.example.post.entity.Category;
import org.example.post.repository.api.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryInMemoryRepository implements CategoryRepository {
    /**
     * Underlying data store. In future should be replaced with database connection.
     */
    private final DataStore store;

    /**
     * @param store data store
     */
    public CategoryInMemoryRepository(DataStore store) {
        this.store = store;
    }


    @Override
    public Optional<Category> find(UUID id) {
        return store.findAllCategories().stream()
                .filter(category -> category.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Category> findAll() {
        return store.findAllCategories();
    }

    @Override
    public void create(Category entity) {
        store.createCategory(entity);
    }

    @Override
    public void delete(Category entity) {
        throw new UnsupportedOperationException("Operation not implemented.");
    }

    @Override
    public void update(Category entity) {
        throw new UnsupportedOperationException("Operation not implemented.");
    }


}
