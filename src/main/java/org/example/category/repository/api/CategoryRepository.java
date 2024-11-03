package org.example.category.repository.api;

import org.example.category.entity.Category;
import org.example.repository.api.Repository;

import java.util.UUID;

public interface CategoryRepository extends Repository<Category, UUID> {
}
