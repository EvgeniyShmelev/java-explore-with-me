package ru.practicum.ewmmain.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.categories.dto.CategoryDto;
import ru.practicum.ewmmain.categories.dto.NewCategoryDto;
import ru.practicum.ewmmain.categories.service.CategoryService;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto createCategory(@RequestBody NewCategoryDto newCategoryDto) {
        log.info("Создание новой категории: {}", newCategoryDto.getName());
        return categoryService.add(newCategoryDto);
    }

    @PatchMapping
    public CategoryDto updateCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Изменена категория {}", categoryDto);
        return categoryService.update(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Удалена категория {}", catId);
        categoryService.delete(catId);
    }
}
