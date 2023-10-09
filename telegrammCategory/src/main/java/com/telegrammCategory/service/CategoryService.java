package com.telegrammCategory.service;

import com.telegrammCategory.model.Category;

import java.util.Collection;
/**  Сервис Категорий  */
public interface CategoryService {
    public String getCategoryLevel(int id);
    public String  getCategoryPreviousLevel(int level);
    public void deleteCategory(String text);
    String viewTree();
    void addTwo( String fatherCategory, String childrenCategory);

    void addOne( String textCommand);

}
