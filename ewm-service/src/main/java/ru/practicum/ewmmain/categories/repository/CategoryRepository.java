package ru.practicum.ewmmain.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.categories.model.Category;
import ru.practicum.ewmmain.users.model.User;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> getCategoryByName(String name);
}
