package com.telegrammCategory;

import com.telegrammCategory.model.Category;
import com.telegrammCategory.repository.CategoryRepository;
import com.telegrammCategory.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    CategoryService categoryService;
    @Mock
    CategoryRepository categoryRepository;
    @Test
    void getCategoryLevelTest() {
        when(categoryRepository.findAllByParent(anyInt())).thenReturn("TV");
        assertThat(categoryService.getCategoryLevel(1)).isEqualTo("TV");
        verify(categoryRepository, times(1)).findAllById(any());
    }
    @Test
    void getCategoryPreviousLevelTest() {
        when(categoryRepository.findPreviousLevel(anyInt())).thenReturn("TV");
        assertThat(categoryService.getCategoryPreviousLevel(1)).isEqualTo("TV");
        verify(categoryRepository, times(1)).findAllById(any());
    }
    @Test
    void greatCategoryTest() {
    when(categoryRepository.findByParentAndMaxSeg(anyInt())).thenReturn(Optional.of(1));
    when(categoryRepository.findAllByParent(anyInt())).thenReturn("TV");
    when(categoryRepository.save(any(Category.class))).thenReturn(any(Category.class));
    assertThat(categoryService.greatCategory(1,"TV")).isEqualTo("TV");
    verify(categoryRepository, times(3)).findAllById(any());
    }
    @Test
    void greatNewCategoryTest() {
        when(categoryRepository.findAllByParent(anyInt())).thenReturn("TV");
        assertThat(categoryService.greatNewCategory(1,"TV")).isEqualTo("TV");
        verify(categoryRepository, times(1)).findAllById(any());
    }

    @Test
    void deleteCategoryTest() {
        Category category1 = new Category();
        category1.setSeq(1);
        category1.setName("TV");
        category1.setParent(1);
        when(categoryRepository.findByParentAndSeg(anyInt(),anyInt())).thenReturn(Optional.of(category1));
        when(categoryRepository.findAllByParent(anyInt())).thenReturn("TV");
        doNothing().when(categoryRepository).deleteById(anyInt());
       verify(categoryRepository, times(3)).findAllById(any());
    }
    @Test
    void newLevelTest() {
        Category category1 = new Category();
        category1.setId(1);
        category1.setSeq(1);
        category1.setName("TV");
        category1.setParent(1);
        when(categoryRepository.findByParentAndSeg(anyInt(),anyInt())).thenReturn(Optional.of(category1));
        when(categoryRepository.findAllByParent(anyInt())).thenReturn("TV");
        when(categoryRepository.save(any(Category.class))).thenReturn(any(Category.class));
        assertThat(categoryService.newLevel(1,1)).isEqualTo(1);
        verify(categoryRepository, times(2)).findAllById(any());
    }

}
