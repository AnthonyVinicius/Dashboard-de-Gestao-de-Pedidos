package com.claro.ordermanager.service;

import com.claro.ordermanager.dto.LoginRequestDTO;
import com.claro.ordermanager.dto.LoginResponseDTO;
import com.claro.ordermanager.entity.User;
import com.claro.ordermanager.exception.InvalidCredentialsException;
import com.claro.ordermanager.repository.UserRepository;
import com.claro.ordermanager.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO dto) {

        log.info("Login attempt for email {}.", dto.email());

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> {
                    log.warn("Login failed. User with email {} was not found.",dto.email());

                    return new InvalidCredentialsException("E-mail ou senha inválidos.");
                });

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {

            log.warn("Login failed. Invalid password for user {}.",user.getId());

            throw new InvalidCredentialsException("E-mail ou senha inválidos.");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getName()
        );

        log.info("User {} authenticated successfully.",user.getId());

        return new LoginResponseDTO(token);
    }
}