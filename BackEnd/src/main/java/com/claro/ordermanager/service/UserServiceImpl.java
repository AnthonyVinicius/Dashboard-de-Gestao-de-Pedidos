package com.claro.ordermanager.service;

import com.claro.ordermanager.dto.UserCreateDTO;
import com.claro.ordermanager.dto.UserResponseDTO;
import com.claro.ordermanager.dto.UserUpdateDTO;
import com.claro.ordermanager.entity.User;
import com.claro.ordermanager.mapper.UserMapper;
import com.claro.ordermanager.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getById(UUID id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return UserMapper.toDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO create(UserCreateDTO dto) {

        if (repository.existsByEmail(dto.email())) {
            throw new RuntimeException("E-mail já cadastrado");
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));

        User saved = repository.save(user);

        return UserMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public UserResponseDTO update(UUID id, UserUpdateDTO dto) {

        User existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        existing.setName(dto.name());

        User saved = repository.save(existing);

        return UserMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void delete(UUID id) {

        User existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        repository.delete(existing);
    }
}