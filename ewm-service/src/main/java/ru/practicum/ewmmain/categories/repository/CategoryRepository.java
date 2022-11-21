package ru.practicum.ewmmain.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.categories.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
