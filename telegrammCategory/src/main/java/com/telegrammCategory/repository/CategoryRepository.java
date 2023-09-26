package com.telegrammCategory.repository;

import com.telegrammCategory.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository  extends JpaRepository< Integer, Category> {
}
