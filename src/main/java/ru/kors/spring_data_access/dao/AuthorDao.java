package ru.kors.spring_data_access.dao;

import ru.kors.spring_data_access.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    void insert(Author author);
    void deleteById(Long id);
    List<Author> findAll();
    Optional<Author> findById(Long id);
}
