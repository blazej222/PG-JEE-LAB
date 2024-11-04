package org.example.category.repository.persistence;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.category.entity.Category;
import org.example.category.repository.api.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Dependent
public class CategoryPersistenceRepository implements CategoryRepository {
    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Category> find(UUID id) {
        return Optional.ofNullable(em.find(Category.class, id));
    }

    @Override
    public List<Category> findAll() {
        return em.createQuery("select f from Category f", Category.class).getResultList();
    }

    @Override
    public void create(Category entity) {
        em.persist(entity);
    }

    @Override
    public void delete(Category entity) {
        em.remove(em.find(Category.class, entity.getId()));
    }

    @Override
    public void update(Category entity) {
        em.merge(entity);
    }
}
