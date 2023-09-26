package com.telegrammCategory.repository;

import com.telegrammCategory.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CategoryRepository  extends JpaRepository< Integer, Category> {
    @Query(nativeQuery = true, value = "SELECT seq FROM сategoryes WHERE parent = :level AND MAX(seq)")
    int findByParentAndMaxSeg(int level);
    @Query(nativeQuery = true, value = "SELECT name FROM сategoryes WHERE parent = (SELECT DISTINCT parent FROM сategoryes WHERE id= :level )")
    Collection<String> findByParent(int level);
    @Query(nativeQuery = true, value = "SELECT * FROM сategoryes WHERE parent = :level AND seq = :id")
    Category findByParentAndSeg(int level, int id);
}
