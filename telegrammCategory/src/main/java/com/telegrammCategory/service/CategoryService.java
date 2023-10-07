package com.telegrammCategory.service;

import com.telegrammCategory.model.Category;

import java.util.Collection;
/**  Сервис Категорий  */
public interface CategoryService {
    public String getCategoryLevel(int id);
    public String  getCategoryPreviousLevel(int level);
    public String  greatCategory(int level,String name);
    public String  greatNewCategory(int id,String name);
    public void deleteCategory(int id,int level);

    public int newLevel(Integer level, int value);

    String viewTree();
}
