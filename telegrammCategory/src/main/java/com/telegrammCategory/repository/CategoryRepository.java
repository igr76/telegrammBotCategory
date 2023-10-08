package com.telegrammCategory.repository;

import com.telegrammCategory.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{

    @Query(nativeQuery = true, value = "SELECT name FROM сategoryes WHERE parent = :level ")
    CharSequence findAllByParent(int level);
    @Query(nativeQuery = true, value = "SELECT name FROM сategoryes WHERE parent = (SELECT DISTINCT parent FROM сategoryes WHERE id= :level )")
    CharSequence findPreviousLevel(int level);
    @Query(nativeQuery = true, value = "SELECT MAX(seq) FROM сategoryes WHERE parent = :level ")
    Optional<Integer> findByParentAndMaxSeg(int level);
    @Query(nativeQuery = true, value = "SELECT * FROM сategoryes WHERE parent = :level AND seq = :id")
    Optional<Category> findByParentAndSeg(int level, int id);
    @Query(nativeQuery = true, value = "SELECT  * FROM сategoryes ORDER BY id DESC LIMIT 1 ")
    Optional<Category> findLastElement();
    @Query(nativeQuery = true, value = "SELECT  * FROM сategoryes WHERE name = :fatherCategory ")
    Optional<Category> findByNname(String fatherCategory);

    @Query(nativeQuery = true, value = "SELECT  COUNT FROM сategoryes WHERE parent = :parent)")
    int existsByParent(Integer parent);

}
