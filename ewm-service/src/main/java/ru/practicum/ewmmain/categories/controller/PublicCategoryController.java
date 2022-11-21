package ru.practicum.ewmmain.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.categories.dto.CategoryDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Collection<CategoryDto> getAllCategories(@PositiveOrZero
                                                    @RequestParam(defaultValue = "0") Integer from,
                                                    @Positive
                                                    @RequestParam(defaultValue = "10") Integer size) {
        log.info("Has received request to endpoint GET/categories?from={}size={}", from, size);
        return categoryService.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("Has received request to endpoint GET/categories/{}", catId);
        return categoryService.getCategoryDtoById(catId);
    }
}
