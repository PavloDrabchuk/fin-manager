package com.example.personalfinancemanager.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.personalfinancemanager.model.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
}
