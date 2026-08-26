package com.example.Tatkal.Service;

import com.example.Tatkal.Entity.Users;
import com.example.Tatkal.Repositry.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository usersRepository;

    @Transactional
    public Users create(Users user) {

        if (usersRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException(
                    "Email already exists"
            );
        }

        return usersRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Users getById(Long id) {

        return usersRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
    }

    @Transactional(readOnly = true)
    public Users getByEmail(String email) {

        return usersRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
    }

    @Transactional
    public Users update(
            Long id,
            Users updated
    ) {

        Users user = getById(id);

        user.setName(updated.getName());

        return usersRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {

        if (!usersRepository.existsById(id)) {
            throw new RuntimeException(
                    "User not found"
            );
        }

        usersRepository.deleteById(id);
    }
}