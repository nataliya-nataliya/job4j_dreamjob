package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.repository.UserRepository;

import java.util.Optional;

@Service
@ThreadSafe
public class SimpleUserService implements UserService {
    private final UserRepository userRepository;

    private SimpleUserService(UserRepository sql2oCandidateRepository) {
        this.userRepository = sql2oCandidateRepository;
    }

    @Override
    public Optional<User> save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public boolean deleteAll() {
        return userRepository.deleteAll();
    }
}
