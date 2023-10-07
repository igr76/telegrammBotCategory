package com.telegrammCategory.service.impl;



import com.telegrammCategory.exception.ElemNotFound;
import com.telegrammCategory.model.Category;
import com.telegrammCategory.repository.CategoryRepository;
import com.telegrammCategory.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**  Сервис Категорий  */


@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;
    private Set<Integer> u= new HashSet<>();
    private char ch = ' ';
    private List<String> categoryStringList = new ArrayList<>();

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @Override
    public String getCategoryLevel(int level) {
        return String.join("\n", categoryRepository.findAllByParent(level));
    }

    @Override
    public String getCategoryPreviousLevel(int level) {
        return String.join("\n",categoryRepository.findPreviousLevel(level));
    }

    @Override
    public String greatCategory(int level, String name) {
        int maxSeq =1;
        try {
            maxSeq = categoryRepository.findByParentAndMaxSeg(level).orElseThrow(ElemNotFound::new);
        } catch (ElemNotFound e) {
        }

        Category category1 = new Category();
        category1.setParent(level);
        category1.setSeq(maxSeq++);
        category1.setName(name);
        categoryRepository.save(category1);
        return getCategoryLevel(level);
    }

    @Override
    public String greatNewCategory(int id, String name) {
        Category category1 = new Category();
        category1.setSeq(1);
        category1.setName(name);
        category1.setParent(id);
        return getCategoryLevel(id);
    }

    @Override
    public void deleteCategory(int id, int level) {
        Category category1 = categoryRepository.findByParentAndSeg(level, id).orElseThrow(ElemNotFound::new);
        if (category1 != null) {
            categoryRepository.delete(category1);
        }

    }

    @Override
    public int newLevel(Integer level, int value) {
        Category category1 = categoryRepository.findByParentAndSeg(level,value).orElseThrow(ElemNotFound::new);
        category1.setName(category1.getName() + " >");
        categoryRepository.save(category1);
        return category1.getId();
    }

    @Override
    public String viewTree() {
        categoryStringList.clear();
        int l=0;
        List<Category> categoryList = categoryRepository.findAll();
        for (int i = 0; i < categoryList.size()-1; i++) {
            if (1==0){tree(categoryList,l);}

            if (u.contains(i)) { break; }else tree(categoryList,i);
        }
        return String.join("\n",categoryStringList);
    }
    void tree(List<Category> e,int l) {
        for (int m = 0; m <e.size()-1; m++) {
            if (e.get(m).getParent() == e.get(l).getId()) {
                l=e.get(m).getId();
                ch +=ch;
                categoryStringList.add(ch+e.get(l).getName());
                u.add(l);
            }

        }
        backTree(e,l);
    }

    void backTree(List<Category> e,int l) {
        while (l <= 1) {
            for (int i = 0; i < e.size()-1; i++) {
                if (e.get(i).getParent() == l) {
                    categoryStringList.add(ch+e.get(l).getName());
                    u.add(l);
                }
            }
            ch-=ch; l=e.get(l).getParent();
        }
    }
}
