package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryVacancyRepository implements VacancyRepository {

    private final AtomicInteger nextId = new AtomicInteger(1);
    private final Map<Integer, Vacancy> vacancies = new HashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "Job description Intern Java Developer",
                LocalDateTime.of(2023, 6, 28, 14, 30, 0), true));
        save(new Vacancy(0, "Junior Java Developer", "Job description Junior Java Developer",
                LocalDateTime.of(2023, 5, 28, 20, 0, 0), true));
        save(new Vacancy(0, "Junior+ Java Developer", "Job description Junior+ Java Developer",
                LocalDateTime.of(2023, 6, 28, 14, 20, 0), true));
        save(new Vacancy(0, "Middle Java Developer", "Job description Middle Java Developer",
                LocalDateTime.of(2023, 6, 27, 14, 30, 0), false));
        save(new Vacancy(0, "Middle+ Java Developer", "Job description Middle+ Java Developer",
                LocalDateTime.of(2023, 6, 28, 10, 10, 0), true));
        save(new Vacancy(0, "Senior Java Developer", "Job description Senior Java Developer",
                LocalDateTime.of(2023, 6, 28, 9, 50, 0), true));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId.getAndIncrement());
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(), (id, oldVacancy)
                -> new Vacancy(oldVacancy.getId(), vacancy.getTitle(),
                vacancy.getDescription(), vacancy.getCreationDate(), vacancy.getVisible())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}
