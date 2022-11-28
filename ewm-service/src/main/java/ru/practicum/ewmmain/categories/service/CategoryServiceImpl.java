package ru.practicum.ewmmain.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewmmain.categories.dto.CategoryDto;
import ru.practicum.ewmmain.categories.dto.NewCategoryDto;
import ru.practicum.ewmmain.categories.model.Category;
import ru.practicum.ewmmain.categories.repository.CategoryRepository;
import ru.practicum.ewmmain.events.model.Event;
import ru.practicum.ewmmain.events.repository.EventRepository;
import ru.practicum.ewmmain.exception.ForbiddenException;
import ru.practicum.ewmmain.exception.NotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        log.info("Получение списка всех категорий");
        Pageable pageable = PageRequest.of(from, size, Sort.by("id"));
        return categoryRepository.findAll(pageable).stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryDtoById(Long id) {
        log.info("Получение категории по id {}", id);
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Category with id %s has not been found", id)));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto add(NewCategoryDto newCategoryDto) {
        log.info("Добавление категории: {}", newCategoryDto);
        Category addedCategory = categoryRepository.save(modelMapper.map(newCategoryDto, Category.class));
        return modelMapper.map(addedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        CategoryDto categoryForUpdate = getCategoryDtoById(categoryDto.getId());
        categoryForUpdate.setName(categoryDto.getName());
        categoryRepository.save(modelMapper.map(categoryForUpdate, Category.class));
        log.info("Обновлена категория с id {}", categoryDto.getId());
        return modelMapper.map(categoryForUpdate, CategoryDto.class);
    }

    @Override
    public void delete(Long id) {
        log.info("Удаление категория с id {}", id);
        CategoryDto categoryDto = getCategoryDtoById(id);
        Category category = modelMapper.map(categoryDto, Category.class);
        Collection<Event> eventsByCategory = eventRepository.findAllByCategoryId(id);
        if (eventsByCategory.isEmpty()) {
            categoryRepository.delete(category);
            log.info("Категория с id {} удалена", id);
        } else {
            throw new ForbiddenException(String.format("Category with id %s contains events", id));
        }
    }
}
