package org.example.post.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.example.controller.servlet.exception.NotFoundException;
import org.example.post.entity.Category;
import org.example.post.entity.Post;
import org.example.post.repository.api.CategoryRepository;
import org.example.post.repository.api.PostRepository;

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
        public void create(Category category) {
            repository.create(category);
        }

        public void delete(UUID id) {
            Category tmp = repository.find(id).orElseThrow(NotFoundException::new);
            List<Post> posts = postRepository.findAllByCategory(tmp);
            for (Post post : posts) {
                postRepository.delete(post);
            }
            repository.delete(tmp);
        }

}

