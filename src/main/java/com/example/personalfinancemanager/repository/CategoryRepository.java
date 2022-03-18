package com.example.personalfinancemanager.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.personalfinancemanager.model.Category;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

}
