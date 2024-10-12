package org.example.post.service;

import org.example.post.entity.Category;
import org.example.post.repository.api.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryService {
        /**
         * Repository for category entity.
         */
        private final CategoryRepository repository;

        /**
         * @param repository repository for category entity
         */
        public CategoryService(CategoryRepository repository) {
            this.repository = repository;
        }

        /**
         * @param id category's id
         * @return container with category entity
         */
        public Optional<Category> find(UUID id) {
            return repository.find(id);
        }

        /**
         * @return all available categories
         */
        public List<Category> findAll() {
            return repository.findAll();
        }

        /**
         * Stores new category in the data store.
         *
         * @param category new category to be saved
         */
        public void create(Category category) {
            repository.create(category);
        }

}

