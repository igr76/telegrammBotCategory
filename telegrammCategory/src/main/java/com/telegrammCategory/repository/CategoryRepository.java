package com.telegrammCategory.repository;

import com.telegrammCategory.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

//@Repository
public class CategoryRepository {
    public int findByParentAndMaxSeg(int level) {
        return 1;
    }//} extends JpaRepository< Category, Integer> {

    public void save(Category category1) {

    }

    public Category findByParentAndSeg(int level, int id) {
        return null;
    }

    public void delete(Category category1) {
    }

    public int findPreviousLevel(int level) {
        return 1;
    }

    public CharSequence findByParent(int level) {
        return "null";
    }
//    @Query(nativeQuery = true, value = "SELECT seq FROM сategoryes WHERE parent = :level AND MAX(seq)")
//    int findByParentAndMaxSeg(int level);
//    @Query(nativeQuery = true, value = "SELECT name FROM сategoryes WHERE parent = (SELECT DISTINCT parent FROM сategoryes WHERE id= :level )")
//    Collection<String> findByParent(int level);
//    @Query(nativeQuery = true, value = "SELECT * FROM сategoryes WHERE parent = :level AND seq = :id")
//    Category findByParentAndSeg(int level, int id);
//    @Query(nativeQuery = true, value = "SELECT parent FROM сategoryes WHERE id = :level")
//    Integer findPreviousLevel(int level);
}
