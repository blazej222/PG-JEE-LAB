package org.example.post.repository.persistence;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.category.entity.Category;
import org.example.post.entity.Post;
import org.example.post.repository.api.PostRepository;
import org.example.user.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Dependent
public class PostPersistenceRepository implements PostRepository {
    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Post> findAllByUser(User user) {
        return em.createQuery("select u from Post u where u.user = :user", Post.class)
                .setParameter("user", user)
                .getResultList();

    }

    @Override
    public List<Post> findAllByCategory(Category Category) {
        return em.createQuery("select u from Post u where u.category = :Category", Post.class)
                .setParameter("Category", Category)
                .getResultList();
    }

    @Override
    public Optional<Post> find(UUID id) {
        return Optional.ofNullable(em.find(Post.class, id));
    }

    @Override
    public List<Post> findAll() {
        return em.createQuery("select u from Post u", Post.class).getResultList();
    }

    @Override
    public void create(Post entity) {
        em.persist(entity);
    }

    @Override
    public void delete(Post entity) {
        em.remove(em.find(Post.class, entity.getId()));
    }

    @Override
    public void update(Post entity) {
        em.merge(entity);
    }
}
