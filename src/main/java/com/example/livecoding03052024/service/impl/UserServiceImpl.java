package com.example.livecoding03052024.service.impl;

import com.example.livecoding03052024.dto.request.UserRequestDto;
import com.example.livecoding03052024.dto.response.UserPostResponseDto;
import com.example.livecoding03052024.dto.response.UserResponseDto;
import com.example.livecoding03052024.model.User;
import com.example.livecoding03052024.model.UserRole;
import com.example.livecoding03052024.repository.UserRepository;
import com.example.livecoding03052024.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public void insert(UserRequestDto userRequestDto) {
        User user = new User();
        BeanUtils.copyProperties(userRequestDto, user);
        user.setPassword(bCryptPasswordEncoder.encode(userRequestDto.password()));
        user.setRoles(userRequestDto.roles().stream()
                .map(string -> UserRole.builder()
                        .user(user)
                        .role(string).build()).toList());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(Integer id, UserRequestDto userRequestDto) {
        User user = userRepository
                .findById(id)
                .orElseThrow();
        BeanUtils.copyProperties(userRequestDto, user);
        user.setId(id);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow();
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public UserResponseDto getById(Integer id) {
        return userRepository.findById(id)
                .map(user -> new UserResponseDto(user.getUsername(), user.getEmail(),
                        new UserPostResponseDto(
                                user.getPost().getTitle(),
                                user.getPost().getContent(),
                                user.getPost().getCreatedDate())))
                .orElseThrow();
    }

    @Override
    @Transactional
    public List<UserResponseDto> getAll() {
        List<UserResponseDto> userResponseDtoList = userRepository.findAll()
                .stream()
                .map(user -> {
                    UserResponseDto userResponse;
                    if (user.getPost() == null) {
                        userResponse = new UserResponseDto(user.getUsername(), user.getEmail(), null);
                    } else {
                        userResponse = new UserResponseDto(user.getUsername(), user.getEmail(), new UserPostResponseDto(
                                user.getPost().getTitle(),
                                user.getPost().getContent(),
                                user.getPost().getCreatedDate()));
                    }
                    return userResponse;
                })
                .toList();

        return userResponseDtoList;
    }
}
