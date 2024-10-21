package org.example.datastore.component;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.example.post.entity.Category;
import org.example.post.entity.Post;
import org.example.serialization.component.CloningUtility;
import org.example.user.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * For the sake of simplification instead of using real database this example is using a data source object which should
 * be put in servlet context in a single instance. In order to avoid {@link java.util.ConcurrentModificationException}
 * all methods are synchronized. Normally synchronization would be carried on by the database server. Caution, this is
 * very inefficient implementation but can be used to present other mechanisms without obscuration example with ORM
 * usage.
 */
@Log
@ApplicationScoped
@NoArgsConstructor(force = true)
public class DataStore {

    /**
     * Set of all available categories.
     */
    private final Set<Category> categories = new HashSet<>();

    /**
     * Set of all characters.
     */
    private final Set<Post> posts = new HashSet<>();

    /**
     * Set of all users.
     */
    private final Set<User> users = new HashSet<>();

    /**
     * Component used for creating deep copies.
     */
    private final CloningUtility cloningUtility;

    /**
     * @param cloningUtility component used for creating deep copies
     */
    @Inject
    public DataStore(CloningUtility cloningUtility) {
        this.cloningUtility = cloningUtility;
    }

    /**
     * Seeks for all categories.
     *
     * @return list (can be empty) of all categories
     */
    public synchronized List<Category> findAllCategories() {
        return categories.stream()
                .map(cloningUtility::clone)
                .collect(Collectors.toList());
    }

    /**
     * Stores new category.
     *
     * @param value new category to be stored
     * @throws IllegalArgumentException if category with provided id already exists
     */
    public synchronized void createCategory(Category value) throws IllegalArgumentException {
        if (categories.stream().anyMatch(category -> category.getId().equals(value.getId()))) {
            throw new IllegalArgumentException("The category id \"%s\" is not unique".formatted(value.getId()));
        }
        categories.add(cloningUtility.clone(value));
    }

    /**
     * Seeks for all posts.
     *
     * @return list (can be empty) of all posts
     */
    public synchronized List<Post> findAllPosts() {
        return posts.stream()
                .map(cloningUtility::clone)
                .collect(Collectors.toList());
    }

    /**
     * Stores new post.
     *
     * @param value new post to be stored
     * @throws IllegalArgumentException if post with provided id already exists or when {@link User} or
     *                                  {@link Category} with provided uuid does not exist
     */
    public synchronized void createPost(Post value) throws IllegalArgumentException {
        if (posts.stream().anyMatch(post -> post.getId().equals(value.getId()))) {
            throw new IllegalArgumentException("The post id \"%s\" is not unique".formatted(value.getId()));
        }
        Post entity = cloneWithRelationships(value);
        posts.add(entity);

        //This ensures that post will be added to category list after creation

        Category tmp = entity.getCategory();
        List<Post> postCategoryList = new java.util.ArrayList<>(findAllPosts().stream().filter(category -> category.getId().equals(tmp.getId())).toList());
        postCategoryList.add(entity);
        tmp.setPosts(postCategoryList);
        updateCategory(tmp);
    }

    /**
     * Updates existing character.
     *
     * @param value post to be updated
     * @throws IllegalArgumentException if post with the same id does not exist or when {@link User} or
     *                                  {@link Category} with provided uuid does not exist
     */
    public synchronized void updatePost(Post value) throws IllegalArgumentException {
        Post entity = cloneWithRelationships(value);
        if (posts.removeIf(post -> post.getId().equals(value.getId()))) {
            posts.add(entity);
        } else {
            throw new IllegalArgumentException("The post with id \"%s\" does not exist".formatted(value.getId()));
        }
        //This ensures that post will be added to category list after creation

        Category tmp = entity.getCategory();
        List<Post> postCategoryList = new java.util.ArrayList<>(findAllPosts().stream().filter(category -> category.getId().equals(tmp.getId())).toList());
        postCategoryList.add(entity);
        tmp.setPosts(postCategoryList);
        updateCategory(tmp);
    }

    /**
     * Deletes existing post.
     *
     * @param id id of post to be deleted
     * @throws IllegalArgumentException if post with provided id does not exist
     */
    public synchronized void deletePost(UUID id) throws IllegalArgumentException {
        if (!posts.removeIf(posts -> posts.getId().equals(id))) {
            throw new IllegalArgumentException("The character with id \"%s\" does not exist".formatted(id));
        }
    }

    /**
     * Seeks for all users.
     *
     * @return list (can be empty) of all users
     */
    public synchronized List<User> findAllUsers() {
        return users.stream()
                .map(cloningUtility::clone)
                .collect(Collectors.toList());
    }

    /**
     * Stores new user.
     *
     * @param value new user to be stored
     * @throws IllegalArgumentException if user with provided id already exists
     */
    public synchronized void createUser(User value) throws IllegalArgumentException {
        if (users.stream().anyMatch(character -> character.getId().equals(value.getId()))) {
            throw new IllegalArgumentException("The user id \"%s\" is not unique".formatted(value.getId()));
        }
        users.add(cloningUtility.clone(value));
    }

    /**
     * Updates existing user.
     *
     * @param value user to be updated
     * @throws IllegalArgumentException if user with the same id does not exist
     */
    public synchronized void updateUser(User value) throws IllegalArgumentException {
        if (users.removeIf(character -> character.getId().equals(value.getId()))) {
            users.add(cloningUtility.clone(value));
        } else {
            throw new IllegalArgumentException("The user with id \"%s\" does not exist".formatted(value.getId()));
        }
    }

    public synchronized void deleteUser(User value) throws IllegalArgumentException {
        if (users.removeIf(character -> character.getId().equals(value.getId()))) {
            return;
        } else {
            throw new IllegalArgumentException("The user with id \"%s\" does not exist".formatted(value.getId()));
        }
    }

    public synchronized void deleteCategory(Category value) throws IllegalArgumentException{
        if(categories.removeIf(category -> category.getId().equals(value.getId()))) {
            return;
        }else{
            throw new IllegalArgumentException("The category with id \"%s\" does not exist".formatted(value.getId()));
        }
    }

    public synchronized void updateCategory(Category value) throws IllegalArgumentException {
        if (categories.removeIf(category -> category.getId().equals(value.getId()))) {
            categories.add(cloningUtility.clone(value));
        }else{
            throw new IllegalArgumentException("The category with id \"%s\" does not exist".formatted(value.getId()));
        }
    }

    /**
     * Clones existing post and updates relationships for values in storage
     *
     * @param value post
     * @return cloned value with updated relationships
     * @throws IllegalArgumentException when {@link User} or {@link Category} with provided uuid does not exist
     */
    private Post cloneWithRelationships(Post value) {
        Post entity = cloningUtility.clone(value);

        if (entity.getUser() != null) {
            entity.setUser(users.stream()
                    .filter(user -> user.getId().equals(value.getUser().getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No user with id \"%s\".".formatted(value.getUser().getId()))));
        }

        if (entity.getCategory() != null) {
            entity.setCategory(categories.stream()
                    .filter(profession -> profession.getId().equals(value.getCategory().getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No category with id \"%s\".".formatted(value.getCategory().getId()))));
        }

        return entity;
    }

}
