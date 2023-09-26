package com.telegrammCategory.service.impl;

import com.telegrammCategory.model.Category;
import com.telegrammCategory.repository.CategoryRepository;
import com.telegrammCategory.service.CategoryService;
import org.jvnet.hk2.annotations.Service;

/**  Сервис Категорий  */
@Service
public class CategoryServiceImpl implements CategoryService {
    private  Category category;
    private CategoryRepository categoryRepository;
    @Override
    public String getCategoryLevel(int level) {
        return String.join("\n", categoryRepository.findByParent(level));
    }

    @Override
    public String getCategoryPreviousLevel(int level) {
        return categoryRepository.findById(level).getParent_node_id;
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
        categoryRepository.delete(category1.getId());
    }

    @Override
    public int newLevel(Integer level, int value) {
        Category category1 = categoryRepository.findByParentAndSeg(level,value);
        category1.setName(category1.getName() + " >");
        categoryRepository.save(category1);
        return category1.getId();
    }
}
