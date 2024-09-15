package ru.kors.spring_data_access.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.kors.spring_data_access.model.Author;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository("jdbc-template-author-dao")
public class JdbcTemplateAuthorDao implements AuthorDao{
    private final JdbcTemplate jdbcTemplate;
    private final static String INSERT_QUERY = "INSERT INTO author(first_name, last_name, birth_date) VALUES(?, ?, ?)";
    private final static String REMOVE_QUERY = "DELETE FROM author WHERE id=?";
    private final static String FIND_ALL_QUERY = "SELECT id, first_name, last_name, birth_date FROM author";
    private final static String FIND_BY_ID_QUERY = "SELECT id, first_name, last_name, birth_date FROM author WHERE id=?";

    public JdbcTemplateAuthorDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insert(Author author) {
        jdbcTemplate.update(psc -> {
            var statement = psc.prepareStatement(INSERT_QUERY);
            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastName());
            statement.setDate(3, Date.valueOf(author.getBirthDate()));
            return statement;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(REMOVE_QUERY, id);
    }

    @Override
    public List<Author> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, (rs, rowNum) -> toAuthor(rs));
    }

    @Override
    public Optional<Author> findById(Long id) {
        try {
            var author = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, (rs, rowNum) -> toAuthor(rs), id);
            return Optional.of(author);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Author toAuthor(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("id");
        var firstName = resultSet.getString("first_name");
        var lastName = resultSet.getString("last_name");
        var birthDate = resultSet.getDate("birth_date").toLocalDate();
        return new Author(id, firstName, lastName, birthDate);
    }
}
