package ru.practicum.ewmmain.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.compilation.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

}
