package ru.practicum.ewmmain.categories.service;

import ru.practicum.ewmmain.categories.dto.CategoryDto;
import ru.practicum.ewmmain.categories.dto.NewCategoryDto;

import java.util.Collection;

public interface CategoryService {
    Collection<CategoryDto> getAll(int from, int size);

    CategoryDto getCategoryDtoById(Long id);

    CategoryDto add(NewCategoryDto newCategoryDto);

    CategoryDto update(CategoryDto categoryDto);

    void delete(Long id);
}
