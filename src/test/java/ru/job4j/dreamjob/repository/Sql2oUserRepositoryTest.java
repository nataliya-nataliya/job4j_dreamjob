package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;

import java.util.Properties;

class Sql2oUserRepositoryTest {
    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oVacancyRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");
        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);
        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearVacancies() {
        sql2oUserRepository.deleteAll();
    }

    @Test
    public void whenAddUniqueEmail() {
        var user = sql2oUserRepository.save(new User(0, "email@nonexistentmail.com",
                "name", "password"));
        var savedUser = sql2oUserRepository.findByEmailAndPassword(
                "email@nonexistentmail.com", "password");
        Assertions.assertEquals(user, savedUser);
    }

    @Test
    public void whenAddSameEmail() {
        var user1 = sql2oUserRepository.save(new User(0, "email@nonexistentmail.com",
                "name", "password"));
        var user2 = sql2oUserRepository.save(new User(0, "email@nonexistentmail.com",
                "name", "password"));
        Assertions.assertTrue(user2.isEmpty());
    }
}
