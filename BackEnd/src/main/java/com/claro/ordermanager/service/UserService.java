package com.claro.ordermanager.service;

import com.claro.ordermanager.dto.UserCreateDTO;
import com.claro.ordermanager.dto.UserResponseDTO;
import com.claro.ordermanager.entity.User;
import com.claro.ordermanager.mapper.UserMapper;
import com.claro.ordermanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAll() {

        log.info("Listing all users.");

        return repository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getById(UUID id) {

        log.info("Searching user with id {}.", id);

        User user = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("User {} not found.", id);
                    return new RuntimeException("Usuário não encontrado");
                });

        return UserMapper.toDTO(user);
    }


    @Transactional
    public UserResponseDTO create(UserCreateDTO dto) {

        log.info("Creating user with email {}.", dto.email());

        if (repository.existsByEmail(dto.email())) {

            log.warn("User creation denied. Email {} already exists.", dto.email());

            throw new RuntimeException("E-mail já cadastrado");
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));

        User saved = repository.save(user);

        log.info("User created successfully. ID={}", saved.getId());

        return UserMapper.toDTO(saved);
    }

    @Transactional
    public void delete(UUID id) {

        log.info("Deleting user {}.", id);

        User existing = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("User {} not found.", id);
                    return new RuntimeException("Usuário não encontrado");
                });

        repository.delete(existing);

        log.info("User {} deleted successfully.", id);
    }
}