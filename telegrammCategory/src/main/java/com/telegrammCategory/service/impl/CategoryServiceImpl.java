package com.telegrammCategory.service.impl;

import com.telegrammCategory.exception.ElemNotFound;
import com.telegrammCategory.model.Category;
import com.telegrammCategory.repository.CategoryRepository;
import com.telegrammCategory.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;


/**  Сервис Категорий  */
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private  Category category;
    private CategoryRepository categoryRepository;


    @Override
    public String getCategoryLevel(int level) {
        try {
            Collection<String> getCategory = categoryRepository.findByParent(level);
            return String.join("\n", getCategory);
        }catch (ElemNotFound e){return "Это меню пустое";}

    }

    @Override
    public int getCategoryPreviousLevel(int level) {
        return categoryRepository.findPreviousLevel(level);
    }

    @Override
    public String greatCategory(int level, String name) {
        int maxSeq = categoryRepository.findByParentAndMaxSeg(level);
        Category category1 = new Category();
        category1.setSeq(category1.getSeq()+1);
        category1.setName(name);
        categoryRepository.save(category1);
        return getCategoryLevel(level);
    }

    @Override
    public String greatNewCategory(int id, String name) {
        Category category1 = new Category();
        category1.setSeq(1);
        category1.setName(name);
        category1.setParent_node_id(id);
        return getCategoryLevel(id);
    }

    @Override
    public void deleteCategory(int id, int level) {
        Category category1 = categoryRepository.findByParentAndSeg(level,id);
        categoryRepository.delete(category1);
    }

    @Override
    public int newLevel(Integer level, int value) {
        Category category1 = categoryRepository.findByParentAndSeg(level,value);
        category1.setName(category1.getName() + " >");
        categoryRepository.save(category1);
        return category1.getId();
    }
}
