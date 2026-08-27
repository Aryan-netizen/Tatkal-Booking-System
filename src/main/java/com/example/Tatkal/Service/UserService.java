package com.example.Tatkal.Service;

import com.example.Tatkal.Dto.UserCreateDTO;
import com.example.Tatkal.Dto.UsersDTO;
import com.example.Tatkal.Entity.Users;
import com.example.Tatkal.Repositry.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository usersRepository;
    private final DTOMapperService mapperService;

    @Transactional
    public UsersDTO create(UserCreateDTO createDTO) {

        if (usersRepository.existsByEmail(createDTO.getEmail())) {
            throw new RuntimeException(
                    "Email already exists"
            );
        }

        Users user = mapperService.toUserEntity(createDTO);
        user.setCreatedAt(OffsetDateTime.now());
        // TODO: Hash password properly before saving
        // user.setPasswordHash(passwordEncoder.encode(createDTO.getPassword()));

        Users savedUser = usersRepository.save(user);
        return mapperService.toUserDTO(savedUser);
    }

    @Transactional(readOnly = true)
    public UsersDTO getById(Long id) {

        Users user = usersRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        return mapperService.toUserDTO(user);
    }

    @Transactional(readOnly = true)
    public List<UsersDTO> findAll() {

        List<Users> users = usersRepository.findAll();
        return mapperService.toUserDTOList(users);

    }

    @Transactional(readOnly = true)
    public UsersDTO getByEmail(String email) {

        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        return mapperService.toUserDTO(user);
    }

    @Transactional
    public UsersDTO update(Long id, UsersDTO updatedDTO) {

        Users user = usersRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        user.setName(updatedDTO.getName());
        user.setEmail(updatedDTO.getEmail());

        Users savedUser = usersRepository.save(user);
        return mapperService.toUserDTO(savedUser);
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