package org.example.category.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.example.category.entity.Category;
import org.example.post.entity.Post;
import org.example.category.repository.api.CategoryRepository;
import org.example.post.repository.api.PostRepository;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class CategoryService {
        /**
         * Repository for category entity.
         */
        private final CategoryRepository repository;
        private final PostRepository postRepository;

        /**
         * @param repository repository for category entity
         */
        @Inject
        public CategoryService(CategoryRepository repository, PostRepository postRepository) {
            this.repository = repository;
            this.postRepository = postRepository;
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
        @Transactional
        public void create(Category category) {
            repository.create(category);
        }

        @Transactional
        public void delete(UUID id) {
            Category tmp = repository.find(id).orElseThrow(NotFoundException::new);
            repository.delete(tmp);
        }

        @Transactional
        public void update(Category category) {
            repository.update(category);
        }

}

